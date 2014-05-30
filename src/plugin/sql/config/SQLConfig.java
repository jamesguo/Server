package plugin.sql.config;

/**
 * Created by yrguo on 14-5-29.
 */
public class SQLConfig {
    private static SQLConfig SQLConfigInstance;
    public String mysqlHost = "127.0.0.1";
    public int mysqlPort = 3306;
    public String mysqlDB = "AutoTestDB";
    public String mysqlUser = "root";
    public String mysqlPassword = "";

    public static SQLConfig getInstance() {
        if (SQLConfigInstance == null) {
            SQLConfigInstance = new SQLConfig();
        }
        return SQLConfigInstance;
    }
}
