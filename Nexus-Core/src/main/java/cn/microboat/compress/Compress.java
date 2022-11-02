package cn.microboat.compress;

import cn.microboat.annotation.SPI;

/**
 * 压缩接口
 *
 * @author zhouwei
 */
@SPI
public interface Compress {

    /**
     * 压缩
     *
     * @param bytes 二进制流
     * @return 二进制流
     */
    byte[] compress(byte[] bytes);

    /**
     * 解压缩
     *
     * @param bytes 二进制流
     * @return 二进制流
     */
    byte[] deCompress(byte[] bytes);
}
