package cn.microboat;

import cn.microboat.annotation.RpcReference;
import org.springframework.stereotype.Component;

/**
 * Hello 控制器
 *
 * @author zhouwei
 */
@Component
public class HelloController {

    @RpcReference(group = "test1", version = "version1.0")
    private HelloService helloService;

    public String test() {
        Hello hello = new Hello();
        hello.setMessage("i am message");
        hello.setDescription("i am description");
        return this.helloService.sayHello(hello);
    }
}
