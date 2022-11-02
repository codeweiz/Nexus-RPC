package cn.microboat.config;

/**
 * RPC 服务配置
 *
 * @author zhouwei
 */
public class RpcServiceConfig {

    /**
     * 服务版本号
     */
    private String version = "";

    /**
     * 服务分组
     */
    private String group = "";

    /**
     * 服务
     */
    private Object service;

    /**
     * 获取 RPC 服务名
     */
    public String getRpcServiceName() {
        return this.getServiceName() + this.getGroup() + this.getVersion();
    }

    /**
     * 获取服务名
     */
    public String getServiceName() {
        return this.service.getClass().getInterfaces()[0].getCanonicalName();
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Object getService() {
        return service;
    }

    public void setService(Object service) {
        this.service = service;
    }
}
