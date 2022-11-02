package cn.microboat.registry.impl;

import cn.microboat.utils.CuratorUtils;
import cn.microboat.registry.ServiceRegistry;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;

/**
 * zookeeper 实现的服务注册
 *
 * @author zhouwei
 */
public class ZkServiceRegistryImpl implements ServiceRegistry {
    /**
     * 在注册中心注册服务
     *
     * @param rpcServiceName    RPC 服务名称
     * @param inetSocketAddress 网络地址
     */
    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        // 拼接服务路径结点
        String servicePath = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName + inetSocketAddress.toString();
        // 获取 zookeeper 客户端
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        // 创建永久性结点
        CuratorUtils.createPersistentNode(zkClient, servicePath);
    }
}
