package cn.microboat.loadbalance;

import cn.microboat.annotation.SPI;
import cn.microboat.pojo.RpcRequest;

import java.util.List;

/**
 * 负载均衡接口
 *
 * @author zhouwei
 */
@SPI
public interface LoadBalance {

    /**
     * 选择服务的地址
     *
     * @param serviceUrlList 注册中心服务提供者列表
     * @param rpcRequest     RPC 请求
     * @return 可以调用的服务地址 url
     */
    String selectServiceAddress(List<String> serviceUrlList, RpcRequest rpcRequest);
}
