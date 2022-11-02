package cn.microboat.loadbalance;

import cn.hutool.core.util.ObjectUtil;
import cn.microboat.pojo.RpcRequest;

import java.util.List;

/**
 * 负载均衡抽象类
 *
 * @author zhouwei
 */
public abstract class AbstractLoadBalance implements LoadBalance {

    /**
     * 选择服务的地址
     *
     * @param serviceUrlList 注册中心服务提供者列表
     * @param rpcRequest     RPC 请求
     * @return 可以调用的服务地址 url
     */
    @Override
    public String selectServiceAddress(List<String> serviceUrlList, RpcRequest rpcRequest) {

        // 如果服务提供者地址列表为空，直接返回 null
        if (ObjectUtil.isEmpty(serviceUrlList)) {
            return null;
        }

        // 如果服务提供者地址列表中只有一个值
        if (serviceUrlList.size() == 1) {
            return serviceUrlList.get(0);
        }
        return doSelect(serviceUrlList, rpcRequest);
    }


    /**
     * 做选择
     *
     * @param serviceUrlList 注册中心服务提供者列表
     * @param rpcRequest     RPC 请求
     * @return 可以调用的服务地址 url
     */
    protected abstract String doSelect(List<String> serviceUrlList, RpcRequest rpcRequest);
}
