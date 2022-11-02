package cn.microboat.exception;

import cn.microboat.enums.RpcErrorMessageEnum;

/**
 * 自定义 RPC 异常
 *
 * @author zhouwei
 */
public class RpcException extends RuntimeException {

    /**
     * @param rpcErrorMessageEnum RPC错误信息枚举
     * @param detail              详情
     */
    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum, String detail) {
        super(rpcErrorMessageEnum.getMessage() + ":" + detail);
    }

    /**
     * @param message 异常信息
     * @param cause   异常
     */
    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param rpcErrorMessageEnum RPC错误信息枚举
     */
    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum) {
        super(rpcErrorMessageEnum.getMessage());
    }
}
