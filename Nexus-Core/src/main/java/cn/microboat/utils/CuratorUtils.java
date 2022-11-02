package cn.microboat.utils;

import cn.microboat.enums.RpcConfigEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouwei
 */
@Slf4j
public class CuratorUtils {


    /**
     * 基础休眠时间
     */
    private static final int BASE_SLEEP_TIME = 1000;

    /**
     * 最大重试次数
     */
    private static final int MAX_RETRIES = 3;

    /**
     * Zookeeper 注册根路径
     */
    public static final String ZK_REGISTER_ROOT_PATH = "/nexus";

    /**
     * 服务地址列表
     */
    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();

    /**
     * 已注册服务路径 Set
     */
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();

    /**
     * 默认 zookeeper 地址
     */
    private static final String DEFAULT_ZOOKEEPER_ADDRESS = "127.0.0.1:2181";

    /**
     * zookeeper 客户端
     */
    private static CuratorFramework zkClient;

    private CuratorUtils() {
    }

    /**
     * 创建永久结点
     *
     * @param zkClient zookeeper 客户端
     * @param path     路径
     */
    public static void createPersistentNode(CuratorFramework zkClient, String path) {
        try {
            // 如果参数路径在已注册服务集中，或者参数 path 在 zookeeper 中已经存在
            if (REGISTERED_PATH_SET.contains(path) || zkClient.checkExists().forPath(path) != null) {
                log.info("该结点：{}已存在", path);
            } else {
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                log.info("结点：{}创建成功", path);
            }
            REGISTERED_PATH_SET.add(path);
        } catch (Exception e) {
            log.error("创建永久结点：{}失败：{}", path, e.getMessage());
        }
    }

    /**
     * 获取子节点
     */
    public static List<String> getChildrenNodes(CuratorFramework zkClient, String rpcServiceName) {
        // 如果 RPC 服务名称在服务地址Map中已存在键对应
        if (SERVICE_ADDRESS_MAP.containsKey(rpcServiceName)) {
            return SERVICE_ADDRESS_MAP.get(rpcServiceName);
        }
        List<String> result = null;
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        try {
            result = zkClient.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(rpcServiceName, result);
            registerWatcher(zkClient, rpcServiceName);
        } catch (Exception e) {
            log.error("获取结点：{}的子结点发生异常：{}", servicePath, e.getMessage());
        }
        return result;
    }

    /**
     * 清理所有注册数据
     * */
    public static void clearRegistry(CuratorFramework zkClient, InetSocketAddress inetSocketAddress) {
        REGISTERED_PATH_SET.stream().parallel().forEach(path -> {
            if (path.endsWith(inetSocketAddress.toString())) {
                try {
                    zkClient.delete().forPath(path);
                } catch (Exception e) {
                    log.error("删除结点：{}注册数据异常：{}", path, e.getMessage());
                }
            }
        });
        log.info("注册服务清理完毕");
    }

    /**
     * 获取 zookeeper 客户端
     */
    public static CuratorFramework getZkClient() {
        Properties properties = PropertiesFileUtils.readPropertiesFile(RpcConfigEnum.RPC_CONFIG_PATH.getPropertyValue());
        String zookeeperAddress = properties != null && properties.getProperty(RpcConfigEnum.ZK_ADDRESS.getPropertyValue()) != null ? properties.getProperty(RpcConfigEnum.ZK_ADDRESS.getPropertyValue()) : DEFAULT_ZOOKEEPER_ADDRESS;
        if (zkClient != null && zkClient.getState() == CuratorFrameworkState.STARTED) {
            return zkClient;
        }
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(zookeeperAddress)
                .retryPolicy(retry)
                .build();
        zkClient.start();

        try {
            if (!zkClient.blockUntilConnected(30, TimeUnit.SECONDS)) {
                log.error("zookeeper 连接超时");
            }
        } catch (InterruptedException e) {
            log.error("zookeeper 连接超时");
        }
        return zkClient;
    }

    /**
     * 注册监听者 Watcher
     */
    private static void registerWatcher(CuratorFramework zkClient, String rpcServiceName) {
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, servicePath, true);
        PathChildrenCacheListener pathChildrenCacheListener = (client, event) -> {
            List<String> serviceAddress = zkClient.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(rpcServiceName, serviceAddress);
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        try {
            pathChildrenCache.start();
        } catch (Exception e) {
            log.error("pathChildrenCache 启动失败：{}", e.getMessage());
        }

    }
}
