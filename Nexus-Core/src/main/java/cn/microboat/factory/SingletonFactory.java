package cn.microboat.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例工厂
 *
 * @author zhouwei
 */
public class SingletonFactory {

    private static final Map<String, Object> OBJECT_MAP = new ConcurrentHashMap<>();

    private SingletonFactory() {
    }

    /**
     * 获取实例方式
     *
     * @param clazz 类
     * @return T
     */
    public static <T> T getInstance(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException();
        }

        String key = clazz.toString();

        if (OBJECT_MAP.containsKey(key)) {
            return clazz.cast(OBJECT_MAP.get(key));
        } else {
            return clazz.cast(OBJECT_MAP.computeIfAbsent(key, k -> {
                try {
                    return clazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }));
        }
    }
}
