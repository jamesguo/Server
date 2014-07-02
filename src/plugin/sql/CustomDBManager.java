package plugin.sql;


import java.sql.SQLException;

import plugin.sql.config.SQLConfig;
import plugin.sql.util.ConnectionPool.PooledConnection;
import plugin.sql.util.ConnectionPool;

/**
 * Created by yrguo on 14-5-28.
 */
public class CustomDBManager {
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
        String connStr = String.format("jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=UTF-8", SQLConfig.getInstance().mysqlHost, SQLConfig.getInstance().mysqlPort, SQLConfig.getInstance().mysqlDB);
        connectionPool = new ConnectionPool("com.mysql.jdbc.Driver", connStr, SQLConfig.getInstance().mysqlUser, SQLConfig.getInstance().mysqlPassword);
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

