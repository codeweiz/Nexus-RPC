package cn.microboat;

import cn.microboat.annotation.RpcScan;
import cn.microboat.config.RpcServiceConfig;
import cn.microboat.server.NettyRpcServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zhouwei
 */
@RpcScan(basePackage = {"cn.microboat"})
public class NettyServerApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyServerApplication.class);
        NettyRpcServer nettyRpcServer = (NettyRpcServer) applicationContext.getBean("nettyRpcServer");

        HelloServiceImpl helloService = new HelloServiceImpl();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        rpcServiceConfig.setService(helloService);
        rpcServiceConfig.setGroup("test1");
        rpcServiceConfig.setVersion("version1.0");

        nettyRpcServer.registerService(rpcServiceConfig);
        nettyRpcServer.start();
    }
}
