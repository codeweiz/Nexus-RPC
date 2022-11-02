package cn.microboat.serialize.kryo;

import cn.microboat.pojo.RpcRequest;
import cn.microboat.pojo.RpcResponse;
import cn.microboat.serialize.Serializer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Kryo 实现序列化和反序列化
 *
 * @author zhouwei
 */
@Slf4j
public class KryoSerializer implements Serializer {

    /**
     * kryo 线程不安全，需要存放在 ThreadLocal 中
     */
    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(RpcResponse.class);
        kryo.register(RpcRequest.class);
        kryo.register(Object.class);
        return kryo;
    });

    /**
     * 序列化
     *
     * @param obj 待序列化的对象
     * @return byte 数组
     */
    @Override
    public byte[] serialize(Object obj) {
        try (
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Output output = new Output(byteArrayOutputStream)
        ) {
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (Exception e) {
            log.error("序列化出现异常：{}", e.getMessage());
        }
        return null;
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
        try (
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                Input input = new Input(byteArrayInputStream)
        ) {
            Kryo kryo = kryoThreadLocal.get();
            Object o = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return clazz.cast(o);
        } catch (Exception e) {
            log.error("反序列化出现异常：{}", e.getMessage());
        }
        return null;
    }
}
