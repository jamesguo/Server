package plugin.sql;


import java.sql.SQLException;
import plugin.sql.util.ConnectionPool.PooledConnection;
import plugin.sql.util.ConnectionPool;
import plugin.sql.config.Config;
/**
 * Created by yrguo on 14-5-28.
 */
public class CustomDBManager {
    String url = "jdbc:mysql://127.0.0.1:3306/AutoTestDB";
    String user = "root";
    String password = "";
    private static PooledConnection conn;
    private static ConnectionPool connectionPool;
    private static CustomDBManager inst;

    public void close() {
        try {
            connectionPool.closeConnectionPool();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CustomDBManager() {
        if (inst != null)
            return;
        String connStr = String.format("jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=UTF-8", Config.getInstance().mysqlHost, Config.getInstance().mysqlPort, Config.getInstance().mysqlDB);
        connectionPool = new ConnectionPool("com.mysql.jdbc.Driver", connStr, Config.getInstance().mysqlUser, Config.getInstance().mysqlPassword);
        try {
            connectionPool.createPool();
            inst = this;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static PooledConnection getConnection() {
        if (inst == null){
            inst = new CustomDBManager();
        }
        try {
            conn = connectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }
}

