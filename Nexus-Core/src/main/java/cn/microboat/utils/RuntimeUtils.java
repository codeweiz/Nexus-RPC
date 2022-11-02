package cn.microboat.utils;

/**
 * 运行时工具类
 *
 * @author zhouwei
 */
public class RuntimeUtils {

    /**
     * 获取 CPU 的最大有效核心数
     */
    public static int cpus() {
        return Runtime.getRuntime().availableProcessors();
    }
}
