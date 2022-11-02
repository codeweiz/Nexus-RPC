package cn.microboat.transport.impl;

import cn.microboat.extension.ExtensionLoader;
import cn.microboat.pojo.RpcRequest;
import cn.microboat.registry.ServiceDiscovery;
import cn.microboat.transport.RpcRequestTransport;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Socket 实现 RPC 请求传输
 *
 * @author zhouwei
 */
@Slf4j
public class SocketRpcRequestTransportImpl implements RpcRequestTransport {

    private final ServiceDiscovery serviceDiscovery;

    public SocketRpcRequestTransportImpl() {
        this.serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("zk");
    }

    /**
     * 发送 RPC 请求
     *
     * @param rpcRequest RPC 请求
     * @return Object 结果
     */
    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        // 通过服务发现，找到可以调用的服务地址
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest);
        try (Socket socket = new Socket()) {
            // socket 和服务地址 建立连接
            socket.connect(inetSocketAddress);
            // 把 RPC 请求发送出去
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(rpcRequest);
            // 获取 RPC 响应
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return objectInputStream.readObject();
        } catch (Exception e) {
            log.error("发送 RPC 请求时出现异常：{}", e.getMessage());
        }
        return null;
    }
}
