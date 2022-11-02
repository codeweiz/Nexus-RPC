package cn.microboat;

import cn.microboat.config.RpcServiceConfig;
import cn.microboat.server.SocketRpcServer;

/**
 * @author zhouwei
 */
public class SocketServerApplication {
    public static void main(String[] args) {
        HelloServiceImpl helloService = new HelloServiceImpl();
        SocketRpcServer socketRpcServer = new SocketRpcServer();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        rpcServiceConfig.setService(helloService);
        socketRpcServer.registerService(rpcServiceConfig);
        socketRpcServer.start();
    }
}
