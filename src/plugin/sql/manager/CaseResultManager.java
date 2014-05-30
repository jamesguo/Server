package plugin.sql.manager;

import plugin.sql.CustomDBManager;
import plugin.sql.bean.ExcuteResultModel;
import plugin.sql.util.ConnectionPool;

/**
 * Created by yrguo on 14-5-30.
 */
public class CaseResultManager {

    public static void replaceExcuteResult(ExcuteResultModel excuteResultModel){
        ConnectionPool.PooledConnection conn = CustomDBManager.getConnection();
        try {
            String sql = "REPLACE INTO CaseResult (batch_number,case_number,device_name,case_excute_state,case_result_path,case_start_time,case_end_time) VALUES ('"
                    + excuteResultModel.batch_number + "','" + excuteResultModel.case_number + "','" + excuteResultModel.device_name + "','" + excuteResultModel.case_excute_state.code + "','" + excuteResultModel.case_result_path + "','" + excuteResultModel.case_start_time+ "','" + excuteResultModel.case_end_time+ "');";
            conn.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }
}
