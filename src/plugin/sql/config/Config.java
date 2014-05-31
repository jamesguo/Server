package plugin.sql.config;

import plugin.sql.util.ConnectionPool;

/**
 * Created by yrguo on 14-5-29.
 */
public class Config {
    private static Config configInstance;
    public String mysqlHost = "127.0.0.1";
    public int mysqlPort = 3306;
    public String mysqlDB = "AutoTestDB";
    public String mysqlUser = "root";
    public String mysqlPassword = "";

    public static Config getInstance() {
        if (configInstance == null) {
            configInstance = new Config();
        }
        return configInstance;
    }
}
