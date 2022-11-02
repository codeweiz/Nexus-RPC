package cn.microboat.utils;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.microboat.config.CustomThreadPoolConfig;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 线程池工厂类
 *
 * @author zhouwei
 */
public class ThreadPoolFactoryUtils {

    private static final Map<String, ExecutorService> THREAD_POOLS = new ConcurrentHashMap<>();

    private ThreadPoolFactoryUtils() {
    }

    public static ExecutorService createCustomThreadPoolIfAbsent(String threadNamePrefix) {
        CustomThreadPoolConfig customThreadPoolConfig = new CustomThreadPoolConfig();
        return createCustomThreadPoolIfAbsent(threadNamePrefix, customThreadPoolConfig);
    }

    public static ExecutorService createCustomThreadPoolIfAbsent(String threadNamePrefix, CustomThreadPoolConfig customThreadPoolConfig) {
        return createCustomThreadPoolIfAbsent(threadNamePrefix, customThreadPoolConfig, false);
    }

    public static ExecutorService createCustomThreadPoolIfAbsent(String threadNamePrefix, CustomThreadPoolConfig customThreadPoolConfig, Boolean daemon) {
        ExecutorService executorService = THREAD_POOLS.computeIfAbsent(threadNamePrefix, k -> createThreadPool(threadNamePrefix, customThreadPoolConfig, daemon));
        // 如果 threadPool 被 shutdown 或者 terminate 的话就重新创建一个
        if (executorService.isShutdown() || executorService.isTerminated()) {
            THREAD_POOLS.remove(threadNamePrefix);
            executorService = createThreadPool(threadNamePrefix, customThreadPoolConfig, daemon);
            THREAD_POOLS.put(threadNamePrefix, executorService);
        }
        return executorService;
    }


    private static ExecutorService createThreadPool(String threadNamePrefix, CustomThreadPoolConfig customThreadPoolConfig, Boolean daemon) {
        ThreadFactory threadFactory = createThreadFactory(threadNamePrefix, daemon);
        return new ThreadPoolExecutor(
                CustomThreadPoolConfig.DEFAULT_CORE_POOL_SIZE,
                CustomThreadPoolConfig.DEFAULT_MAXIMUM_POOL_SIZE,
                CustomThreadPoolConfig.DEFAULT_KEEP_ALIVE_TIME,
                CustomThreadPoolConfig.DEFAULT_TIME_UNIT,
                CustomThreadPoolConfig.DEFAULT_BLOCKING_QUEUE,
                threadFactory);
    }

    public static ThreadFactory createThreadFactory(String threadNamePrefix, Boolean daemon) {
        if (threadNamePrefix != null) {
            if (daemon != null) {
                return new ThreadFactoryBuilder()
                        .setNamePrefix(threadNamePrefix)
                        .setDaemon(daemon).build();
            } else {
                return new ThreadFactoryBuilder()
                        .setNamePrefix(threadNamePrefix).build();
            }
        }
        return Executors.defaultThreadFactory();
    }

    public static void shutDownAllThreadPool() {
        THREAD_POOLS.entrySet().parallelStream().forEach(entry -> {
            ExecutorService executorService = entry.getValue();
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(10L, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
            }
        });
    }
}
