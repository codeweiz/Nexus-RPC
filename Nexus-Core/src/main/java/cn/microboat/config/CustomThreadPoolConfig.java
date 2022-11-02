package cn.microboat.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池配置
 *
 * @author zhouwei
 */
public class CustomThreadPoolConfig {

    /**
     * 线程池默认核心池大小
     */
    public static final int DEFAULT_CORE_POOL_SIZE = 10;

    /**
     * 线程池默认最大池大小
     */
    public static final int DEFAULT_MAXIMUM_POOL_SIZE = 100;

    /**
     * 线程池默认保持存活时间
     */
    public static final long DEFAULT_KEEP_ALIVE_TIME = 1L;

    /**
     * 线程池默认时间单位
     */
    public static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;

    /**
     * 线程池默认阻塞队列容量
     */
    public static final int DEFAULT_BLOCKING_QUEUE_CAPACITY = 100;

    /**
     * 有界队列
     */
    public static final BlockingQueue<Runnable> DEFAULT_BLOCKING_QUEUE = new ArrayBlockingQueue<>(DEFAULT_BLOCKING_QUEUE_CAPACITY);
}
