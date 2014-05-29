package plugin.sql.manager;

import plugin.sql.CustomDBManager;
import plugin.sql.bean.ArgumentModel;
import plugin.sql.bean.TestCaseModel;
import plugin.sql.bean.TestCaseStepModel;
import plugin.sql.util.ConnectionPool;

import java.sql.ResultSet;
import java.util.HashMap;

/**
 * Created by yrguo on 14-5-29.
 */
public class ArgumentManager {
    public static HashMap<String,HashMap<String,ArgumentModel>> argumentHashMap = new HashMap<String, HashMap<String, ArgumentModel>>();
    public static void loadALLArgument() {
        ConnectionPool.PooledConnection conn = CustomDBManager.getConnection();
        try {
            ResultSet resultSet = conn.executeQuery("Select * From CaseArgumentTable");
            while (resultSet.next()) {

                ArgumentModel argumentModel = new ArgumentModel();
                argumentModel.name = resultSet.getString("argument_name");
                argumentModel.android_value = resultSet.getString("android_value");
                argumentModel.ios_value = resultSet.getString("ios_value");
                argumentModel.owner = resultSet.getString("argument_owner");
                HashMap<String,ArgumentModel> valueHashMap = argumentHashMap.get(argumentModel.owner);
                if(valueHashMap==null){
                    valueHashMap = new HashMap<String, ArgumentModel>();
                    argumentHashMap.put(argumentModel.owner,valueHashMap);
                }
                valueHashMap.put(argumentModel.name,argumentModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }
    public static String getArgumentRealValue(String argumentName,String owner,String os){
        HashMap<String,ArgumentModel> valueHashMap = argumentHashMap.get(owner);
        if(valueHashMap!=null){
            ArgumentModel model =  valueHashMap.get(argumentName);
            if(model!=null){
                return model.getValue(os);
            }
        }
        return "";
    }
    public static void insertArgument(ArgumentModel argumentModel){
        if(argumentModel!=null){
            ConnectionPool.PooledConnection conn = CustomDBManager.getConnection();
            try {
                conn.executeUpdate("INSERT INTO CaseArgumentTable (argument_name,android_value,ios_value,argument_owner) VALUES ('"+argumentModel.name+"','"+argumentModel.android_value+"','"+argumentModel.ios_value+"','"+argumentModel.owner+"');");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conn.close();
            }
        }
    }

    public static void updateArgument(ArgumentModel argumentModel){
        ConnectionPool.PooledConnection conn = CustomDBManager.getConnection();
        try {
            conn.executeUpdate("INSERT INTO CaseArgumentTable (argument_name,android_value,ios_value,argument_owner) VALUES ('"+argumentModel.name+"','"+argumentModel.android_value+"','"+argumentModel.ios_value+"','"+argumentModel.owner+"');");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }

    public static void deleteArgument(String owner,String name){
        ConnectionPool.PooledConnection conn = CustomDBManager.getConnection();
        try {
            conn.executeUpdate("DELETE CaseArgumentTable.* FROM CaseArgumentTable WHERE CaseArgumentTable.argument_name = '"+name+"' AND CaseArgumentTable.argument_owner = '"+owner+"');");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }
}
