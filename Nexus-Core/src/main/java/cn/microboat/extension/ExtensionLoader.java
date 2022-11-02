package cn.microboat.extension;

import cn.hutool.core.util.StrUtil;
import cn.microboat.annotation.SPI;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 扩展加载器
 *
 * @author zhouwei
 */
@SuppressWarnings("unchecked")
@Slf4j
public class ExtensionLoader<T> {

    /**
     * 服务目录
     */
    private static final String SERVICE_DIRECTORY = "META-INF/extensions/";

    /**
     * key：类
     * value：ExtensionLoader
     */
    private static final Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADER_MAP = new ConcurrentHashMap<>();

    /**
     * key：类
     * value：Object
     */
    private static final Map<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>();

    /**
     * 类
     */
    private final Class<?> type;

    /**
     * 缓存实例
     * <p>
     * key：String
     * value：Holder<Object>
     */
    private final Map<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();

    /**
     * 对 Map 的包装
     */
    private final Holder<Map<String, Class<?>>> cacheClasses = new Holder<>();

    /**
     * 构造器
     */
    public ExtensionLoader(Class<?> type) {
        this.type = type;
    }

    /**
     * 根据 类型 获取扩展加载器
     */
    public static <S> ExtensionLoader<S> getExtensionLoader(Class<S> type) {
        // type 为 null
        if (type == null) {
            throw new IllegalArgumentException("扩展类型不能为空");
        }
        // type 不是接口
        if (!type.isInterface()) {
            throw new IllegalArgumentException("扩展类型必须为接口");
        }
        // type 没有被 @SPI 注解修饰
        if (type.getAnnotation(SPI.class) == null) {
            throw new IllegalArgumentException("扩展类型必须被 @SPI 注解修饰");
        }

        // 从 EXTENSION_LOADER_MAP 中根据 type 获取 ExtensionLoader
        ExtensionLoader<S> extensionLoader = (ExtensionLoader<S>) EXTENSION_LOADER_MAP.get(type);
        // 如果扩展加载器为空
        if (extensionLoader == null) {
            // new 一个扩展加载器，并 put 进 EXTENSION_LOADER_MAP
            EXTENSION_LOADER_MAP.putIfAbsent(type, new ExtensionLoader<S>(type));
            extensionLoader = (ExtensionLoader<S>) EXTENSION_LOADER_MAP.get(type);
        }
        return extensionLoader;
    }

    /**
     * 根据名称获取扩展
     *
     * @param name 名称
     */
    public T getExtension(String name) {
        // 参数为空，抛出非法异常
        if (StrUtil.isBlankIfStr(name)) {
            throw new IllegalArgumentException("参数不能为空");
        }
        // 根据 name 从缓存实例中获取对应的 Holder
        Holder<Object> holder = cachedInstances.get(name);
        // 如果 holder 为空，就 new 一个 Holder
        if (holder == null) {
            cachedInstances.putIfAbsent(name, new Holder<>());
            holder = cachedInstances.get(name);
        }
        // 从 holder 中获取值
        Object value = holder.getValue();
        // 如果 holder 中的值为空
        if (value == null) {
            synchronized (holder) {
                value = holder.getValue();
                if (value == null) {
                    value = createExtension(name);
                    holder.setValue(value);
                }
            }
        }
        return (T) value;
    }

    /**
     * 根据名称创建扩展
     */
    private T createExtension(String name) {
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            throw new RuntimeException("没有" + name + "对应的扩展");
        }
        T instance = (T) EXTENSION_INSTANCES.get(clazz);
        if (instance == null) {
            try {
                EXTENSION_INSTANCES.putIfAbsent(clazz, clazz.newInstance());
                instance = (T) EXTENSION_INSTANCES.get(clazz);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return instance;
    }


    /**
     * 获取扩展类
     */
    private Map<String, Class<?>> getExtensionClasses() {
        Map<String, Class<?>> classMap = cacheClasses.getValue();
        if (classMap == null) {
            synchronized (cacheClasses) {
                classMap = cacheClasses.getValue();
                if (classMap == null) {
                    classMap = new HashMap<>();
                    loadDirectory(classMap);
                    cacheClasses.setValue(classMap);
                }
            }
        }
        return classMap;
    }


    /**
     * 加载目录
     */
    private void loadDirectory(Map<String, Class<?>> extensionClasses) {
        String fileName = ExtensionLoader.SERVICE_DIRECTORY + type.getName();
        try {
            Enumeration<URL> urls;
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            urls = classLoader.getResources(fileName);
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    URL resourceUrl = urls.nextElement();
                    loadResource(extensionClasses, classLoader, resourceUrl);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 加载资源
     */
    private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, URL resourceUrl) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceUrl.openStream(), UTF_8))) {
            String line;
            // read every line
            while ((line = reader.readLine()) != null) {
                // get index of comment
                final int ci = line.indexOf('#');
                if (ci >= 0) {
                    // string after # is comment so we ignore it
                    line = line.substring(0, ci);
                }
                line = line.trim();
                if (line.length() > 0) {
                    try {
                        final int ei = line.indexOf('=');
                        String name = line.substring(0, ei).trim();
                        String clazzName = line.substring(ei + 1).trim();
                        // our SPI use key-value pair so both of them must not be empty
                        if (name.length() > 0 && clazzName.length() > 0) {
                            Class<?> clazz = classLoader.loadClass(clazzName);
                            extensionClasses.put(name, clazz);
                        }
                    } catch (ClassNotFoundException e) {
                        log.error(e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
