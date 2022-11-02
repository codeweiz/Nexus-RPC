package cn.microboat.annotation;

import java.lang.annotation.*;

/**
 * 作用在 RUNTIME 运行时
 * 可加在 类、接口、注解、枚举 上
 *
 * @author zhouwei
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SPI {
}
