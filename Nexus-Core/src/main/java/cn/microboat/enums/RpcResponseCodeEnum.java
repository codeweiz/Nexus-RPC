package cn.microboat.enums;

/**
 * RPC 响应码枚举
 *
 * @author zhouwei
 */
public enum RpcResponseCodeEnum {

    /**
     * 成功
     */
    SUCCESS(200, "The remote procedure call is successful"),

    /**
     * 失败
     */
    FAIL(500, "The remote procedure call is fail");

    private final int code;

    private final String message;

    RpcResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
