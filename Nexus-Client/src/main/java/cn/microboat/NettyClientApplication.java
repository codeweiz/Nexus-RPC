package cn.microboat;

import cn.microboat.annotation.RpcScan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Netty 客户端应用程序
 *
 * @author zhouwei
 */
@RpcScan(basePackage = {"cn.microboat"})
@Slf4j
public class NettyClientApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyClientApplication.class);
        HelloController helloController = (HelloController) applicationContext.getBean("helloController");
        String test = helloController.test();
        log.info("调用结果为：{}", test);
    }
}
