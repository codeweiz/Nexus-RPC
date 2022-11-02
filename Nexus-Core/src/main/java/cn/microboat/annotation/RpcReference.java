package cn.microboat.annotation;

import java.lang.annotation.*;

/**
 * 作用在运行时 @Retention(RetentionPolicy.RUNTIME)
 * 加在字段上 @Target({ElementType.FIELD})
 * 显示注解信息 @Documented
 * 运行时增强 @Inherited
 * 相关信息用来构建 RpcServiceConfig
 * 标注一个 RPC 引用
 *
 * @author zhouwei
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface RpcReference {

    String version() default "";

    String group() default "";
}
