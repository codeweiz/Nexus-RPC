package cn.microboat.serialize.hessian;

import cn.microboat.serialize.Serializer;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * hessian 实现序列化和反序列化
 *
 * @author zhouwei
 */
@Slf4j
public class HessianSerializer implements Serializer {
    /**
     * 序列化
     *
     * @param obj 待序列化的对象
     * @return byte 数组
     */
    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            // 根据 字节数组输出流 创建 hessian 输出流
            HessianOutput hessianOutput = new HessianOutput(byteArrayOutputStream);
            // hessian 输出流 写对象 obj 到 字节数组输出流
            hessianOutput.writeObject(obj);
            // 返回 字节数组输出流 转字节数组
            return byteArrayOutputStream.toByteArray();
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
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            // 根据 字节数组输入流 创建 hessian 输入流
            HessianInput hessianInput = new HessianInput(byteArrayInputStream);
            // hessian 输入流 读取 字符数组 bytes 为 object
            Object o = hessianInput.readObject();
            // 通过 clazz 把 o 转换类型
            return clazz.cast(o);
        } catch (Exception e) {
            log.error("反序列化出现异常：{}", e.getMessage());
        }
        return null;
    }
}
