package cn.microboat.extension;

/**
 * 包装类，提供通用泛型封装
 * volatile 保证可见性、禁止指令重排
 *
 * @author zhouwei
 */
public class Holder<T> {

    /**
     * 泛型 T，value
     */
    private volatile T value;

    public Holder() {
    }

    public Holder(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
