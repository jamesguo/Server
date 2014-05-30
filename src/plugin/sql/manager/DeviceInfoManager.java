package plugin.sql.manager;

import plugin.sql.CustomDBManager;
import plugin.sql.bean.DeviceInfoModel;
import plugin.sql.bean.TestCaseModel;
import plugin.sql.bean.TestCaseStepModel;
import plugin.sql.util.ConnectionPool;

import java.sql.ResultSet;

/**
 * Created by yrguo on 14-5-29.
 */
public class DeviceInfoManager {
    public static void insertDeviceInfo(DeviceInfoModel deviceInfoModel) {
        ConnectionPool.PooledConnection conn = CustomDBManager.getConnection();
        try {
            String sql = "REPLACE INTO DeviceInfoTable (device_name,device_os,device_height,device_width,device_version) VALUES ('" + deviceInfoModel.device_name + "','" + deviceInfoModel.device_os + "','" + deviceInfoModel.device_height + "','" + deviceInfoModel.device_width + "','" + deviceInfoModel.device_version + "');";
            conn.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }
}
