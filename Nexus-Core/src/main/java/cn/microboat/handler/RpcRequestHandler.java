package cn.microboat.handler;

import cn.microboat.factory.SingletonFactory;
import cn.microboat.pojo.RpcRequest;
import cn.microboat.provider.ServiceProvider;
import cn.microboat.provider.impl.ZkServiceProviderImpl;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * RPC 请求处理器
 *
 * @author zhouwei
 */
@Slf4j
public class RpcRequestHandler {

    private final ServiceProvider serviceProvider;

    public RpcRequestHandler() {
        this.serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }

    /**
     * 调用目标方法
     *
     * @param rpcRequest RPC 请求
     * @param service    服务
     * @return Object 结果
     */
    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result = null;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getParameters());
            log.info("调用目标方法：{}成功", rpcRequest.getMethodName());
        } catch (Exception e) {
            log.error("调用目标方法：{}出现异常：{}", rpcRequest.getMethodName(), e.getMessage());
        }
        return result;
    }

    /**
     * 处理 RPC 请求
     *
     * @param rpcRequest RPC 请求
     * @return Object 结果
     */
    public Object handle(RpcRequest rpcRequest) {
        Object service = serviceProvider.getService(rpcRequest.getRpcServiceName());
        return invokeTargetMethod(rpcRequest, service);
    }
}
