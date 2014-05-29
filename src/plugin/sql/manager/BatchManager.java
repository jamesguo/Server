package plugin.sql.manager;

import plugin.sql.CustomDBManager;
import plugin.sql.bean.ArgumentModel;
import plugin.sql.util.ConnectionPool;

import java.sql.ResultSet;
import java.util.HashMap;

/**
 * Created by yrguo on 14-5-29.
 */
public class BatchManager {
    public void getNextReadyBatch(){
        ConnectionPool.PooledConnection conn = CustomDBManager.getConnection();
        try {
            ResultSet resultSet = conn.executeQuery("Select * From BatchTable");
            if (resultSet.next()) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }
}
