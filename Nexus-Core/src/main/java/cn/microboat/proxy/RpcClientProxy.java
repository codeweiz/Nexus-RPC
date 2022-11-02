package cn.microboat.proxy;

import cn.hutool.core.util.IdUtil;
import cn.microboat.config.RpcServiceConfig;
import cn.microboat.enums.RpcErrorMessageEnum;
import cn.microboat.enums.RpcResponseCodeEnum;
import cn.microboat.exception.RpcException;
import cn.microboat.pojo.RpcRequest;
import cn.microboat.pojo.RpcResponse;
import cn.microboat.transport.RpcRequestTransport;
import cn.microboat.transport.impl.NettyRpcRequestTransportImpl;
import cn.microboat.transport.impl.SocketRpcRequestTransportImpl;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;

/**
 * RPC 客户端代理
 *
 * @author zhouwei
 */
@Slf4j
public class RpcClientProxy implements InvocationHandler {

    /**
     * 接口名
     */
    private static final String INTERFACE_NAME = "interfaceName";

    /**
     * RPC 请求传输
     */
    private final RpcRequestTransport rpcRequestTransport;

    /**
     * RPC 服务配置
     */
    private final RpcServiceConfig rpcServiceConfig;

    public RpcClientProxy(RpcRequestTransport rpcRequestTransport, RpcServiceConfig rpcServiceConfig) {
        this.rpcRequestTransport = rpcRequestTransport;
        this.rpcServiceConfig = rpcServiceConfig;
    }

    public RpcClientProxy(RpcRequestTransport rpcRequestTransport) {
        this.rpcRequestTransport = rpcRequestTransport;
        this.rpcServiceConfig = new RpcServiceConfig();
    }

    /**
     * 根据 class 对象 获取 代理对象
     *
     * @param clazz 类
     * @return T 对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    /**
     * 检查 RPC 请求和响应是否有问题
     *
     * @param rpcRequest  RPC 请求
     * @param rpcResponse RPC 响应
     */
    private void checkRpc(RpcRequest rpcRequest, RpcResponse<Object> rpcResponse) {
        // 如果 rpcResponse 为空
        if (rpcResponse == null) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        // 如果 RPC 请求的 请求id 和 RPC 响应的 请求id 不一致
        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            throw new RpcException(RpcErrorMessageEnum.REQUEST_NOT_MATCH_RESPONSE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        // 如果 RPC 响应的状态码为空 或者 不是成功对应的状态码
        if (rpcResponse.getCode() == null || !rpcResponse.getCode().equals(RpcResponseCodeEnum.SUCCESS.getCode())) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("Proxy 调用方法：{}", method.getName());

        // 创建 RPC 请求的实例
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRequestId(IdUtil.fastSimpleUUID());
        rpcRequest.setGroup(rpcServiceConfig.getGroup());
        rpcRequest.setVersion(rpcServiceConfig.getVersion());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameters(args);
        rpcRequest.setParamTypes(method.getParameterTypes());
        rpcRequest.setInterfaceName(method.getDeclaringClass().getName());

        // 初始化 RPC 响应
        RpcResponse<Object> rpcResponse = null;

        // 根据 rpcRequestTransport 的实现方式获取 RPC 响应
        if (rpcRequestTransport instanceof NettyRpcRequestTransportImpl) {
            // 如果实现方式是 Netty，同步非阻塞
            // 使用 CompletableFuture<> 包裹 RpcResponse<Object>
            CompletableFuture<RpcResponse<Object>> completableFuture = (CompletableFuture<RpcResponse<Object>>) rpcRequestTransport.sendRpcRequest(rpcRequest);
            rpcResponse = completableFuture.get();

        } else if (rpcRequestTransport instanceof SocketRpcRequestTransportImpl) {
            // 如果实现方式是 Socket，同步阻塞
            rpcResponse = (RpcResponse<Object>) rpcRequestTransport.sendRpcRequest(rpcRequest);
        }

        // 检查 RPC 请求和响应是否有异常
        this.checkRpc(rpcRequest, rpcResponse);

        // 返回 RPC 响应的数据
        return rpcResponse.getData();
    }
}
