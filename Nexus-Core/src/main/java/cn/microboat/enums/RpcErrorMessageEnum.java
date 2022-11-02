package cn.microboat.enums;

/**
 * RPC 错误信息枚举
 *
 * @author zhouwei
 */
public enum RpcErrorMessageEnum {

    /**
     * 客户端连接服务端失败
     */
    CLIENT_CONNECT_SERVER_FAILURE("客户端连接服务端失败"),

    /**
     * 服务调用失败
     */
    SERVICE_INVOCATION_FAILURE("服务调用失败"),

    /**
     * 没有找到指定的服务
     */
    SERVICE_CANNOT_BE_FOUND("没有找到指定的服务"),

    /**
     * 注册的服务没有实现任何接口
     */
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务没有实现任何接口"),

    /**
     * 请求和返回不匹配
     */
    REQUEST_NOT_MATCH_RESPONSE("请求和返回不匹配");

    private final String message;

    RpcErrorMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
