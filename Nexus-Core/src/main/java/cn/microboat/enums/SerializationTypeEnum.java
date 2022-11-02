package cn.microboat.enums;

/**
 * 序列化类型枚举
 *
 * @author zhouwei
 */
public enum SerializationTypeEnum {

    /**
     * Kryo
     */
    KRYO((byte) 0x01, "kryo"),

    /**
     * protostuff
     */
    PROTOSTUFF((byte) 0x02, "protostuff"),

    /**
     * hessian
     */
    HESSIAN((byte) 0x03, "hessian");

    private final byte code;

    private final String name;

    SerializationTypeEnum(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据 code 获取 name
     *
     * @param code code
     * @return String name
     */
    public static String getNameByCode(byte code) {
        for (SerializationTypeEnum value : SerializationTypeEnum.values()) {
            if (value.getCode() == code) {
                return value.name;
            }
        }
        return null;
    }

    public byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
