package plugin.sql.manager;

import plugin.sql.CustomDBManager;
import plugin.sql.bean.ArgumentModel;
import plugin.sql.bean.BatchModel;
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
            ResultSet resultSet = conn.executeQuery("Select * From BatchTable WHERE batch_state = '1'");
            if (resultSet.next()) {
                BatchModel batchModel = new BatchModel();
                batchModel.batch_number = resultSet.getInt("batch_number");
                batchModel.batch_owner = resultSet.getString("batch_owner");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }
}
