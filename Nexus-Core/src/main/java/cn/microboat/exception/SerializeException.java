package cn.microboat.exception;

/**
 * 自定义序列化异常
 *
 * @author zhouwei
 */
public class SerializeException extends RuntimeException {

    /**
     * @param message 异常信息
     */
    public SerializeException(String message) {
        super(message);
    }
}
