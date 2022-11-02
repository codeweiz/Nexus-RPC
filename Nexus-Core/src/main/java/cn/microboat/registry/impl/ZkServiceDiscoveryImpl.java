package cn.microboat.registry.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.microboat.extension.ExtensionLoader;
import cn.microboat.pojo.RpcRequest;
import cn.microboat.utils.CuratorUtils;
import cn.microboat.loadbalance.LoadBalance;
import cn.microboat.registry.ServiceDiscovery;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Zookeeper 实现的服务发现
 *
 * @author zhouwei
 */
@Slf4j
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {

    private final LoadBalance loadBalance;

    public ZkServiceDiscoveryImpl() {
        this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension("loadBalance");
    }

    /**
     * 根据 RPC 服务名称查找 InetSocketAddress
     *
     * @param rpcRequest RPC 请求体
     * @return InetSocketAddress
     */
    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        String rpcServiceName = rpcRequest.getRpcServiceName();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient, rpcServiceName);
        if (ObjectUtil.isEmpty(serviceUrlList)) {
            log.error("RPC 服务：{} 的可用服务地址为空", rpcServiceName);
        }
        String targetServiceUrl = loadBalance.selectServiceAddress(serviceUrlList, rpcRequest);
        log.info("成功找到可调用服务地址：{}", targetServiceUrl);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}
