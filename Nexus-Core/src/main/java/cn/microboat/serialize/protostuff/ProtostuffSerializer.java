package cn.microboat.serialize.protostuff;

import cn.microboat.serialize.Serializer;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * protostuff 实现序列化和反序列化
 *
 * @author zhouwei
 */
public class ProtostuffSerializer implements Serializer {

    /**
     * 避免每次序列化时重新应用缓冲区空间
     * 默认缓冲区空间大小为 512 长度的 byte 数组
     */
    private static final LinkedBuffer BUFFER = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

    /**
     * 序列化
     *
     * @param obj 待序列化的对象
     * @return byte 数组
     */
    @SuppressWarnings("unchecked, rawtypes")
    @Override
    public byte[] serialize(Object obj) {
        Class<?> clazz = obj.getClass();
        Schema schema = RuntimeSchema.getSchema(clazz);
        byte[] bytes;
        try {
            bytes = ProtostuffIOUtil.toByteArray(obj, schema, BUFFER);
        } finally {
            BUFFER.clear();
        }
        return bytes;
    }

    /**
     * 反序列化
     *
     * @param bytes byte 数组
     * @param clazz 类
     * @return T 类型对象
     */
    @Override
    public <T> T deSerialize(byte[] bytes, Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        return obj;
    }
}
