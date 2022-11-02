package cn.microboat.netty;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Channel 提供者
 *
 * @author zhouwei
 */
@Slf4j
public class ChannelProvider {

    private final Map<String, Channel> channelMap;

    public ChannelProvider() {
        this.channelMap = new ConcurrentHashMap<>();
    }

    /**
     * 根据服务地址获取 Channel
     *
     * @param inetSocketAddress 服务地址
     * @return Channel
     */
    public Channel get(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        if (channelMap.containsKey(key)) {
            Channel channel = channelMap.get(key);
            if (channel != null && channel.isActive()) {
                return channel;
            } else {
                channelMap.remove(key);
            }
        }
        return null;
    }

    /**
     * 添加 key-value
     *
     * @param inetSocketAddress 服务地址
     * @param channel           Channel
     */
    public void set(InetSocketAddress inetSocketAddress, Channel channel) {
        channelMap.put(inetSocketAddress.toString(), channel);
    }

    /**
     * 移除指定 key
     *
     * @param inetSocketAddress 服务地址
     */
    public void remove(InetSocketAddress inetSocketAddress) {
        channelMap.remove(inetSocketAddress.toString());
    }

}
