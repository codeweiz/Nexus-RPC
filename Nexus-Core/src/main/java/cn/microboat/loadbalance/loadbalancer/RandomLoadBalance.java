package cn.microboat.loadbalance.loadbalancer;

import cn.microboat.pojo.RpcRequest;
import cn.microboat.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.Random;

/**
 * 随机负载均衡
 *
 * @author zhouwei
 */
public class RandomLoadBalance extends AbstractLoadBalance {
    /**
     * 做选择
     *
     * @param serviceUrlList 注册中心服务提供者列表
     * @param rpcRequest     RPC 请求
     * @return 可以调用的服务地址 url
     */
    @Override
    protected String doSelect(List<String> serviceUrlList, RpcRequest rpcRequest) {
        // 通过 Random 实例的 nextInt 方法，传入 serviceUrlList.size() 作为随机数的边界
        return serviceUrlList.get(new Random().nextInt(serviceUrlList.size()));
    }
}
