package cn.microboat;

import java.io.Serializable;

/**
 * 实体
 *
 * @author zhouwei
 */
public class Hello implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息
     */
    private String message;

    /**
     * 描述
     */
    private String description;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
