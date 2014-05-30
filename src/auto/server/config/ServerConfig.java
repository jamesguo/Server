package auto.server.config;

import java.util.Properties;

/**
 * Created by yrguo on 14-5-30.
 */
public class ServerConfig {
    private static Properties properties;

    public static String getConfig(String key) {
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(ServerConfig.class.getResourceAsStream("/auto.properties"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            value = new String(value.getBytes("ISO8859-1"), "UTF-8");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return value.trim();
    }
}
