package cn.microboat.constants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * RPC 常量
 *
 * @author zhouwei
 */
public class RpcConstants {

    /**
     * 魔数，占五个字节，用来验证接收的数据符不符合规范
     */
    public static final byte[] MAGIC_NUMBER = {(byte) 'n', (byte) 'e', (byte) 'x', (byte) 'u'};

    /**
     * 默认字符集，UTF-8
     */
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * 版本号
     */
    public static final byte VERSION = 1;

    /**
     * 总长度
     */
    public static final byte TOTAL_LENGTH = 16;

    /**
     * 类型：请求
     */
    public static final byte REQUEST_TYPE = 1;

    /**
     * 类型：响应
     */
    public static final byte RESPONSE_TYPE = 2;

    /**
     * 类型：请求心跳
     */
    public static final byte HEARTBEAT_REQUEST_TYPE = 3;

    /**
     * 类型：响应心跳
     */
    public static final byte HEARTBEAT_RESPONSE_TYPE = 4;

    /**
     * 头长度
     */
    public static final int HEAD_LENGTH = 16;

    /**
     * ping
     */
    public static final String PING = "ping";

    /**
     * pong
     */
    public static final String PONG = "pong";

    /**
     * 最大帧长度
     */
    public static final int MAX_FRAME_LENGTH = 8 * 1024 * 1024;
}
