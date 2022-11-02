package cn.microboat.transport.impl;

import cn.microboat.codec.RpcMessageDecoder;
import cn.microboat.codec.RpcMessageEncoder;
import cn.microboat.constants.RpcConstants;
import cn.microboat.enums.CompressTypeEnum;
import cn.microboat.enums.SerializationTypeEnum;
import cn.microboat.extension.ExtensionLoader;
import cn.microboat.factory.SingletonFactory;
import cn.microboat.handler.NettyRpcClientHandler;
import cn.microboat.netty.ChannelProvider;
import cn.microboat.netty.UnProcessedRequest;
import cn.microboat.pojo.RpcMessage;
import cn.microboat.pojo.RpcRequest;
import cn.microboat.pojo.RpcResponse;
import cn.microboat.registry.ServiceDiscovery;
import cn.microboat.transport.RpcRequestTransport;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Netty 实现 RPC 请求传输
 *
 * @author zhouwei
 */
@Slf4j
public class NettyRpcRequestTransportImpl implements RpcRequestTransport {

    private final ServiceDiscovery serviceDiscovery;
    private final UnProcessedRequest unProcessedRequest;
    private final ChannelProvider channelProvider;
    private final EventLoopGroup eventLoopGroup;
    private final Bootstrap bootstrap;


    public NettyRpcRequestTransportImpl() {
        this.serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("zk");
        this.unProcessedRequest = SingletonFactory.getInstance(UnProcessedRequest.class);
        this.channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
        this.eventLoopGroup = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        this.bootstrap.group(this.eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                        p.addLast(new RpcMessageEncoder());
                        p.addLast(new RpcMessageDecoder());
                        p.addLast(new NettyRpcClientHandler());
                    }
                });
    }

    /**
     * 连接服务器并获取通道，以便可以向服务器发送 RPC 消息
     *
     * @param inetSocketAddress 服务地址
     * @return Channel
     */
    @SneakyThrows
    public Channel doConnect(InetSocketAddress inetSocketAddress) {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        this.bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }

    /**
     * 根据地址获取 Channel
     *
     * @param inetSocketAddress 服务地址
     * @return Channel
     */
    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        Channel channel = channelProvider.get(inetSocketAddress);
        if (channel == null) {
            channel = doConnect(inetSocketAddress);
            channelProvider.set(inetSocketAddress, channel);
        }
        return channel;
    }

    /**
     * 关闭 eventLoopGroup
     */
    public void close() {
        this.eventLoopGroup.shutdownGracefully();
    }


    /**
     * 发送 RPC 请求
     *
     * @param rpcRequest RPC 请求
     * @return Object 结果
     */
    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        CompletableFuture<RpcResponse<Object>> resultFuture = new CompletableFuture<>();
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest);
        Channel channel = getChannel(inetSocketAddress);
        if (channel.isActive()) {
            unProcessedRequest.put(rpcRequest.getRequestId(), resultFuture);
            RpcMessage rpcMessage = new RpcMessage();
            rpcMessage.setCodec(SerializationTypeEnum.HESSIAN.getCode());
            rpcMessage.setCompress(CompressTypeEnum.GZIP.getCode());
            rpcMessage.setMessageType(RpcConstants.REQUEST_TYPE);
            rpcMessage.setData(rpcRequest);

            channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("客户端发送信息：{}", rpcMessage);
                } else {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.error("发送信息出现异常：{}", future.cause().getMessage());
                }
            });
        } else {
            throw new IllegalStateException();
        }
        return resultFuture;
    }
}
