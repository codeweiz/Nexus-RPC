package cn.microboat.enums;

/**
 * RPC 配置枚举
 *
 * @author zhouwei
 */
public enum RpcConfigEnum {

    /**
     * RPC 配置路径
     * */
    RPC_CONFIG_PATH("rpc.properties"),

    /**
     * Zookeeper 地址
     * */
    ZK_ADDRESS("rpc.zookeeper.address");

    private final String propertyValue;

    RpcConfigEnum(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getPropertyValue() {
        return propertyValue;
    }
}
