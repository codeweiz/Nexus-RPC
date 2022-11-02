package cn.microboat.loadbalance.loadbalancer;

import cn.microboat.pojo.RpcRequest;
import cn.microboat.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一致性哈希负载均衡
 *
 * @author zhouwei
 */
public class ConsistentHashLoadBalance extends AbstractLoadBalance {

    private final ConcurrentHashMap<String, ConsistentHashSelector> selectors = new ConcurrentHashMap<>();

    /**
     * 做选择
     *
     * @param serviceUrlList 注册中心服务提供者列表
     * @param rpcRequest     RPC 请求
     * @return 可以调用的服务地址 url
     */
    @Override
    protected String doSelect(List<String> serviceUrlList, RpcRequest rpcRequest) {
        return null;
    }


    /**
     * 自定义一致性哈希选择器
     */
    static class ConsistentHashSelector {
        private final TreeMap<Long, String> virtualInvokers;
        private final int identityHashCode;

        ConsistentHashSelector(List<String> invokers, int replicaNumber, int identityHashCode) {
            this.virtualInvokers = new TreeMap<>();
            this.identityHashCode = identityHashCode;
        }
    }
}
