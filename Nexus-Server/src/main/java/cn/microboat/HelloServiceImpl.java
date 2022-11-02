package cn.microboat;

import cn.microboat.Hello;
import cn.microboat.HelloService;
import cn.microboat.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;

/**
 * Hello 服务接口实现类
 *
 * @author zhouwei
 */
@Slf4j
@RpcService(group = "test1", version = "version1.0")
public class HelloServiceImpl implements HelloService {
    /**
     * 发送 Hello
     *
     * @param hello 实体
     * @return 结果
     */
    @Override
    public String sayHello(Hello hello) {
        log.info("cn.microboat.HelloServiceImpl 收到信息：{}", hello.getMessage());
        log.info("cn.microboat.HelloServiceImpl 收到描述：{}", hello.getDescription());
        return hello.getDescription();
    }
}
