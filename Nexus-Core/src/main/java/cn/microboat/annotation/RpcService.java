package cn.microboat.annotation;

import java.lang.annotation.*;

/**
 * RPC 服务注解，用于标注 RPC 服务的实现类
 * <p>
 * 元注解含义：
 * 1、@Documented 会把类上的所有注解在 JavaDoc 上显示出来
 * 2、@Retention(RetentionPolicy.RUNTIME) 表示作用域在运行时
 * 3、@Target({ElementType.TYPE})表示用于描述类、接口(包括注解类型) 或 enum 声明
 * 4、@Inherited 和 @Retention(RetentionPolicy.RUNTIME) 同时存在，增强继承性，在运行时可通过反射获取
 *
 * @author zhouwei
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface RpcService {

    /**
     * 服务的版本号，默认值为空串
     */
    String version() default "";

    /**
     * 服务的群组，默认值为空串
     */
    String group() default "";
}
