package cn.microboat.handler;

import cn.microboat.factory.SingletonFactory;
import cn.microboat.pojo.RpcRequest;
import cn.microboat.pojo.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Socket RPC 请求处理器
 *
 * @author zhouwei
 */
@Slf4j
public class SocketRpcRequestHandler implements Runnable {

    /**
     * socket 实例
     */
    private final Socket socket;

    /**
     * RPC 请求处理器
     */
    private final RpcRequestHandler rpcRequestHandler;

    public SocketRpcRequestHandler(Socket socket) {
        this.socket = socket;
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }

    @Override
    public void run() {
        log.info("当前线程是：{}", Thread.currentThread().getName());
        try (
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())
        ) {
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            Object result = rpcRequestHandler.handle(rpcRequest);
            objectOutputStream.writeObject(RpcResponse.success(result, rpcRequest.getRequestId()));
            objectOutputStream.flush();
        } catch (Exception e) {
            log.error("Socket RPC 请求处理器出现异常：{}", e.getMessage());
        }
    }
}
