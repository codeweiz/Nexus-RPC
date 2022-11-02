package cn.microboat.registry;

import cn.microboat.annotation.SPI;

import java.net.InetSocketAddress;

/**
 * 服务注册
 *
 * @author zhouwei
 */
@SPI
public interface ServiceRegistry {

    /**
     * 在注册中心注册服务
     *
     * @param rpcServiceName    RPC 服务名称
     * @param inetSocketAddress 网络地址
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);
}
