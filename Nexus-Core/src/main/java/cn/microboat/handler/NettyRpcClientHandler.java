package cn.microboat.handler;

import cn.microboat.constants.RpcConstants;
import cn.microboat.enums.CompressTypeEnum;
import cn.microboat.enums.SerializationTypeEnum;
import cn.microboat.factory.SingletonFactory;
import cn.microboat.netty.UnProcessedRequest;
import cn.microboat.pojo.RpcMessage;
import cn.microboat.pojo.RpcResponse;
import cn.microboat.transport.impl.NettyRpcRequestTransportImpl;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * Netty RPC 客户端处理器
 *
 * @author zhouwei
 */
@Slf4j
public class NettyRpcClientHandler extends ChannelInboundHandlerAdapter {

    private final UnProcessedRequest unProcessedRequest;
    private final NettyRpcRequestTransportImpl nettyRpcRequestTransport;

    public NettyRpcClientHandler() {
        this.unProcessedRequest = SingletonFactory.getInstance(UnProcessedRequest.class);
        this.nettyRpcRequestTransport = SingletonFactory.getInstance(NettyRpcRequestTransportImpl.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            if (msg instanceof RpcMessage) {
                RpcMessage tmp = (RpcMessage) msg;
                byte messageType = tmp.getMessageType();
                if (messageType == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
                    log.info("心跳响应：{}", tmp.getData());
                } else if (messageType == RpcConstants.RESPONSE_TYPE) {
                    RpcResponse<Object> rpcResponse = (RpcResponse<Object>) tmp.getData();
                    unProcessedRequest.complete(rpcResponse);
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                Channel channel = nettyRpcRequestTransport.getChannel((InetSocketAddress) ctx.channel().remoteAddress());
                RpcMessage rpcMessage = new RpcMessage();
                rpcMessage.setCodec(SerializationTypeEnum.PROTOSTUFF.getCode());
                rpcMessage.setCompress(CompressTypeEnum.GZIP.getCode());
                rpcMessage.setMessageType(RpcConstants.HEARTBEAT_REQUEST_TYPE);
                rpcMessage.setData(RpcConstants.PING);
                channel.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("客户端发生异常：{}", cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }
}
