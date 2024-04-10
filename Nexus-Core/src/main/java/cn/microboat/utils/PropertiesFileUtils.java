package cn.microboat.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * 配置文件工具类
 *
 * @author zhouwei
 */
public class PropertiesFileUtils {

    private PropertiesFileUtils() {
    }

    /**
     * 读取配置文件
     */
    public static Properties readPropertiesFile(String fileName) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
        String rpcConfigPath = "";
/*        if (url != null) {
            rpcConfigPath = url.getPath() + fileName;
        }*/
        Properties properties = null;
        try (InputStreamReader inputStreamReader = new InputStreamReader(Files.newInputStream(Paths.get(url.getPath())), StandardCharsets.UTF_8)) {
            properties = new Properties();
            properties.load(inputStreamReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
