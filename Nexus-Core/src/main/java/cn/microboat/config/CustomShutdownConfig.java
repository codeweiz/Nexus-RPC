package cn.microboat.config;

import cn.microboat.constants.Constants;
import cn.microboat.utils.CuratorUtils;
import cn.microboat.utils.ThreadPoolFactoryUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * 自定义关闭配置
 *
 * @author zhouwei
 */
@Slf4j
public class CustomShutdownConfig {

    private static final CustomShutdownConfig CUSTOM_SHUTDOWN_CONFIG = new CustomShutdownConfig();

    public static CustomShutdownConfig getCustomShutdownConfig() {
        return CUSTOM_SHUTDOWN_CONFIG;
    }

    /**
     * 清除所有
     * 包括：zookeeper 上的任务、线程池
     * */
    public void clearAll() {
        log.info("自定义关闭配置的 clearAll() 方法执行");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), Constants.PORT);
                CuratorUtils.clearRegistry(CuratorUtils.getZkClient(), inetSocketAddress);
            } catch (Exception e) {
                log.error("清除 zookeeper 上服务出现异常：{}", e.getMessage());
            }
        }));
        // 关闭线程池
        ThreadPoolFactoryUtils.shutDownAllThreadPool();
    }
}
