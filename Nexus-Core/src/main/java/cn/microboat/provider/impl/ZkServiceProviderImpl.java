package cn.microboat.provider.impl;

import cn.microboat.config.RpcServiceConfig;
import cn.microboat.constants.Constants;
import cn.microboat.enums.RpcErrorMessageEnum;
import cn.microboat.exception.RpcException;
import cn.microboat.extension.ExtensionLoader;
import cn.microboat.provider.ServiceProvider;
import cn.microboat.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Zookeeper 实现 ServiceProvider
 *
 * @author zhouwei
 */
@Slf4j
public class ZkServiceProviderImpl implements ServiceProvider {

    /**
     * 服务Map
     */
    private final Map<String, Object> serviceMap;

    /**
     * 已注册的服务
     */
    private final Set<String> registeredService;

    /**
     * 服务注册
     */
    private final ServiceRegistry serviceRegistry;

    public ZkServiceProviderImpl() {
        this.serviceMap = new ConcurrentHashMap<>();
        this.registeredService = ConcurrentHashMap.newKeySet();
        this.serviceRegistry = ExtensionLoader.getExtensionLoader(ServiceRegistry.class).getExtension("zk");
    }

    /**
     * 添加一个 RPC 服务
     *
     * @param rpcServiceConfig RPC 服务配置
     */
    @Override
    public void addService(RpcServiceConfig rpcServiceConfig) {
        String rpcServiceName = rpcServiceConfig.getRpcServiceName();
        if (!registeredService.contains(rpcServiceName)) {
            registeredService.add(rpcServiceName);
            serviceMap.put(rpcServiceName, rpcServiceConfig.getService());
            log.info("添加服务成功：{}，接口：{}", rpcServiceName, rpcServiceConfig.getService().getClass().getInterfaces());
        }
    }

    /**
     * 根据 RPC 服务名称获取 RPC 服务
     *
     * @param rpcServiceName RPC 服务名称
     * @return Object
     */
    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if (service == null) {
            log.error("没有找到可用服务");
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CANNOT_BE_FOUND);
        }
        log.info("找到可用服务：{}", service);
        return service;
    }

    /**
     * 发布服务
     *
     * @param rpcServiceConfig RPC 服务配置
     */
    @Override
    public void publishService(RpcServiceConfig rpcServiceConfig) {
        try {
            // 获取当前 host
            String host = InetAddress.getLocalHost().getHostAddress();
            // 添加服务
            this.addService(rpcServiceConfig);
            // 在 zookeeper 上注册服务
            serviceRegistry.registerService(rpcServiceConfig.getRpcServiceName(), new InetSocketAddress(host, Constants.PORT));
        } catch (UnknownHostException e) {
            log.error("发布服务失败：{}", e.getMessage());
        }
    }
}
