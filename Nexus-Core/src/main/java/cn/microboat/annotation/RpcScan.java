package cn.microboat.annotation;

import cn.microboat.spring.CustomScannerRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 作用在运行时 @Retention(RetentionPolicy.RUNTIME)
 * 加在类、接口、注解、枚举、方法上 @Target({ElementType.TYPE, ElementType.METHOD})
 * 显示注解信息 @Documented
 *
 * @author zhouwei
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Import(CustomScannerRegister.class)
public @interface RpcScan {

    String[] basePackage();
}
