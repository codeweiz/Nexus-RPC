package cn.microboat;

import cn.microboat.config.RpcServiceConfig;
import cn.microboat.proxy.RpcClientProxy;
import cn.microboat.transport.impl.SocketRpcRequestTransportImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouwei
 */
@Slf4j
public class SocketClientApplication {
    public static void main(String[] args) {
        SocketRpcRequestTransportImpl socketRpcRequestTransport = new SocketRpcRequestTransportImpl();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(socketRpcRequestTransport, rpcServiceConfig);
        HelloService proxy = rpcClientProxy.getProxy(HelloService.class);
        Hello hello = new Hello();
        hello.setMessage("i am socket message");
        hello.setDescription("i am socket description");
        String result = proxy.sayHello(hello);
        log.info("收到服务端传送的结果：{}", result);
    }
}
