package cn.microboat.provider;

import cn.microboat.config.RpcServiceConfig;

/**
 * 服务提供者
 *
 * @author zhouwei
 */
public interface ServiceProvider {

    /**
     * 添加一个 RPC 服务
     *
     * @param rpcServiceConfig RPC 服务配置
     */
    void addService(RpcServiceConfig rpcServiceConfig);

    /**
     * 根据 RPC 服务名称获取 RPC 服务
     *
     * @param rpcServiceName RPC 服务名称
     * @return Object
     */
    Object getService(String rpcServiceName);

    /**
     * 发布服务
     *
     * @param rpcServiceConfig RPC 服务配置
     */
    void publishService(RpcServiceConfig rpcServiceConfig);
}
