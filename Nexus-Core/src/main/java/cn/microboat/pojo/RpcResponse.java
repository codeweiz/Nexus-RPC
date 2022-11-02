package cn.microboat.pojo;

import cn.microboat.enums.RpcResponseCodeEnum;

import java.io.Serializable;

/**
 * RPC 响应
 *
 * @author zhouwei
 */
public class RpcResponse<T> implements Serializable {

    /**
     * 序列号 id
     */
    private static final long serialVersionUID = 715745410605631233L;

    /**
     * 请求 id
     */
    private String requestId;

    /**
     * 响应体 body
     */
    private T data;

    /**
     * 响应码 code
     */
    private Integer code;

    /**
     * 响应消息 message
     */
    private String message;


    /**
     * 成功
     *
     * @param data      响应体 body
     * @param requestId 请求id
     * @return RpcResponse
     */
    public static <T> RpcResponse<T> success(T data, String requestId) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(RpcResponseCodeEnum.SUCCESS.getCode());
        response.setMessage(RpcResponseCodeEnum.SUCCESS.getMessage());
        response.setRequestId(requestId);
        if (null != data) {
            response.setData(data);
        }
        return response;
    }

    /**
     * 失败
     *
     * @param rpcResponseCodeEnum RPC响应码枚举
     * @return RpcResponse
     */
    public static <T> RpcResponse<T> fail(RpcResponseCodeEnum rpcResponseCodeEnum) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(rpcResponseCodeEnum.getCode());
        response.setMessage(rpcResponseCodeEnum.getMessage());
        return response;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "requestId='" + requestId + '\'' +
                ", data=" + data +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
