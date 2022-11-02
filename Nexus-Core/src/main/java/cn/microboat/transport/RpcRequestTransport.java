package cn.microboat.transport;

import cn.microboat.annotation.SPI;
import cn.microboat.pojo.RpcRequest;

/**
 * RPC 请求传输接口
 *
 * @author zhouwei
 */
@SPI
public interface RpcRequestTransport {

    /**
     * 发送 RPC 请求
     *
     * @param rpcRequest RPC 请求
     * @return Object 结果
     */
    Object sendRpcRequest(RpcRequest rpcRequest);
}
