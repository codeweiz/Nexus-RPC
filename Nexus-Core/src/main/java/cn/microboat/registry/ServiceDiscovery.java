package cn.microboat.registry;

import cn.microboat.annotation.SPI;
import cn.microboat.pojo.RpcRequest;

import java.net.InetSocketAddress;

/**
 * 服务发现
 *
 * @author zhouwei
 */
@SPI
public interface ServiceDiscovery {

    /**
     * 根据 RPC 服务名称查找 InetSocketAddress
     *
     * @param rpcRequest RPC 请求体
     * @return InetSocketAddress
     */
    InetSocketAddress lookupService(RpcRequest rpcRequest);
}
