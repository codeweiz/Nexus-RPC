package cn.microboat.enums;

/**
 * 压缩类型枚举
 *
 * @author zhouwei
 */
public enum CompressTypeEnum {

    /**
     * GZIP
     */
    GZIP((byte) 0x01, "gzip");

    /**
     * code 编码
     */
    private final byte code;

    /**
     * name 名称
     */
    private final String name;

    /**
     * 根据 code 获取 name
     *
     * @param code code
     * @return name
     */
    public static String getNameByCode(byte code) {
        for (CompressTypeEnum compressTypeEnum : CompressTypeEnum.values()) {
            if (compressTypeEnum.getCode() == code) {
                return compressTypeEnum.getName();
            }
        }
        return null;
    }

    CompressTypeEnum(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
