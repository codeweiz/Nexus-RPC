package cn.microboat.server;

import cn.hutool.core.thread.ThreadUtil;
import cn.microboat.config.CustomShutdownConfig;
import cn.microboat.config.RpcServiceConfig;
import cn.microboat.constants.Constants;
import cn.microboat.factory.SingletonFactory;
import cn.microboat.utils.ThreadPoolFactoryUtils;
import cn.microboat.handler.SocketRpcRequestHandler;
import cn.microboat.provider.ServiceProvider;
import cn.microboat.provider.impl.ZkServiceProviderImpl;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * Socket RPC 服务
 *
 * @author zhouwei
 */
@Slf4j
public class SocketRpcServer {

    /**
     * 线程池
     */
    private final ExecutorService threadPool;

    /**
     * 服务提供者
     */
    private final ServiceProvider serviceProvider;

    public SocketRpcServer() {
        this.threadPool = ThreadPoolFactoryUtils.createCustomThreadPoolIfAbsent("socket-server-rpc-pool");
        this.serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }


    /**
     * 注册服务
     *
     * @param rpcServiceConfig RPC 服务配置
     */
    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    /**
     * 启动
     */
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            String host = InetAddress.getLocalHost().getHostAddress();
            serverSocket.bind(new InetSocketAddress(host, Constants.PORT));

            // 清除 zookeeper 上的服务、关闭线程池
            CustomShutdownConfig.getCustomShutdownConfig().clearAll();

            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                log.info("socket 客户端连接：{}成功", socket.getInetAddress());
                // 执行 SocketRpcRequestHandler 的 run 方法
                ThreadUtil.execute(new SocketRpcRequestHandler(socket));
//                threadPool.execute(new SocketRpcRequestHandler(socket));
            }
        } catch (Exception e) {
            log.error("Socket RPC 服务出现异常：{}", e.getMessage());
        }
    }
}
