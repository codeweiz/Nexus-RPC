package cn.microboat.serialize;

import cn.microboat.annotation.SPI;

/**
 * 序列化接口
 *
 * @author zhouwei
 */
@SPI
public interface Serializer {

    /**
     * 序列化
     *
     * @param obj 待序列化的对象
     * @return byte 数组
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     *
     * @param bytes byte 数组
     * @param clazz 类
     * @return T 类型对象
     */
    <T> T deSerialize(byte[] bytes, Class<T> clazz);
}
