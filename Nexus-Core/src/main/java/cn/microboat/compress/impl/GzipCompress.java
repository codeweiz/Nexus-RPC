package cn.microboat.compress.impl;

import cn.microboat.compress.Compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZIP 实现压缩与解压缩
 *
 * @author zhouwei
 */
public class GzipCompress implements Compress {

    private static final int BUFFER_SIZE = 1024;

    @Override
    public byte[] compress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }

        try (
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                GZIPOutputStream gzip = new GZIPOutputStream(outputStream);
        ) {
            gzip.write(bytes);
            gzip.flush();
            gzip.finish();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("gzip compress error", e);
        }
    }

    @Override
    public byte[] deCompress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }

        try (
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(bytes));
        ) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int n;
            while ((n = gzipInputStream.read(buffer)) > -1) {
                outputStream.write(buffer, 0, n);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("gzip deCompress error", e);
        }
    }
}
