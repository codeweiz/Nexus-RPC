package cn.microboat.spring;

import cn.microboat.annotation.RpcReference;
import cn.microboat.annotation.RpcService;
import cn.microboat.config.RpcServiceConfig;
import cn.microboat.extension.ExtensionLoader;
import cn.microboat.factory.SingletonFactory;
import cn.microboat.provider.ServiceProvider;
import cn.microboat.provider.impl.ZkServiceProviderImpl;
import cn.microboat.proxy.RpcClientProxy;
import cn.microboat.transport.RpcRequestTransport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * Spring Bean 后置处理器
 *
 * @author zhouwei
 */
@Slf4j
@Component
public class SpringBeanPostProcessor implements BeanPostProcessor {

    private final ServiceProvider serviceProvider;

    private final RpcRequestTransport rpcRequestTransport;

    public SpringBeanPostProcessor() {
        this.serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
        this.rpcRequestTransport = ExtensionLoader.getExtensionLoader(RpcRequestTransport.class).getExtension("netty");
    }

    /**
     * Process 之后 Initialization 之前
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RpcService.class)) {
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
            rpcServiceConfig.setGroup(rpcService.group());
            rpcServiceConfig.setVersion(rpcService.version());
            rpcServiceConfig.setService(bean);
            serviceProvider.publishService(rpcServiceConfig);
        }
        return bean;
    }

    /**
     * Process 之后 Initialization 之后
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = bean.getClass();
        Field[] declaredFields = targetClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            RpcReference rpcReference = declaredField.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
                rpcServiceConfig.setGroup(rpcReference.group());
                rpcServiceConfig.setVersion(rpcReference.version());
                RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcRequestTransport, rpcServiceConfig);
                Object clientProxy = rpcClientProxy.getProxy(declaredField.getType());
                declaredField.setAccessible(true);
                try {
                    declaredField.set(bean, clientProxy);
                } catch (Exception e) {
                    log.error("设置代理出现异常：{}", e.getMessage());
                }
            }
        }
        return bean;
    }
}
