package cn.microboat.netty;

import cn.microboat.pojo.RpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 不需要处理的请求
 *
 * @author zhouwei
 */
public class UnProcessedRequest {

    private static final Map<String, CompletableFuture<RpcResponse<Object>>> UN_PROCESSED_RESPONSE_FUTURES = new ConcurrentHashMap<>();

    /**
     * 添加 key-value
     *
     * @param requestId 请求id
     * @param future    包裹的RPC响应
     */
    public void put(String requestId, CompletableFuture<RpcResponse<Object>> future) {
        UN_PROCESSED_RESPONSE_FUTURES.put(requestId, future);
    }

    /**
     * 完成，从 map 中移除指定 key-value
     *
     * @param rpcResponse RPC 响应
     */
    public void complete(RpcResponse<Object> rpcResponse) {
        CompletableFuture<RpcResponse<Object>> future = UN_PROCESSED_RESPONSE_FUTURES.remove(rpcResponse.getRequestId());
        if (null != future) {
            future.complete(rpcResponse);
        } else {
            throw new IllegalStateException();
        }
    }
}
