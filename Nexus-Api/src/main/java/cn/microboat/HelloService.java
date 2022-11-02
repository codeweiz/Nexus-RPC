package cn.microboat;

/**
 * Hello 服务接口
 *
 * @author zhouwei
 */
public interface HelloService {

    /**
     * 发送 Hello
     *
     * @param hello 实体
     * @return 结果
     */
    String sayHello(Hello hello);
}
