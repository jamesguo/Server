package plugin.sql.config;

/**
 * Created by yrguo on 14-5-29.
 */
public class SQLConfig {
    private static SQLConfig SQLConfigInstance;
    public String mysqlHost = "10.2.254.204";
    public int mysqlPort = 3306;
    public String mysqlDB = "uiautodb";
    public String mysqlUser = "root";
    public String mysqlPassword = "liuwj";

    public static SQLConfig getInstance() {
        if (SQLConfigInstance == null) {
            SQLConfigInstance = new SQLConfig();
        }
        return SQLConfigInstance;
    }
}
