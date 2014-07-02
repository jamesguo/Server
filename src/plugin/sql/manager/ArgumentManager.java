package plugin.sql.manager;

import plugin.sql.CustomDBManager;
import plugin.sql.bean.ArgumentModel;
import plugin.sql.bean.TestCaseModel;
import plugin.sql.bean.TestCaseStepModel;
import plugin.sql.util.ConnectionPool;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yrguo on 14-5-29.
 */
public class ArgumentManager {
    public static HashMap<String,HashMap<String,ArgumentModel>> argumentHashMap = new HashMap<String, HashMap<String, ArgumentModel>>();

    public static void main(String[] args) {
        ArrayList<ArgumentModel> argumentModels = new ArrayList<ArgumentModel>();

        ArgumentModel argumentModel0 = new ArgumentModel();
        argumentModel0.ios_value = "CtripInfoBar";
        argumentModel0.android_value="CtripInfoBar";
        argumentModel0.name="$CtripInfoBar";
        argumentModel0.owner="James";
        argumentModels.add(argumentModel0);

        ArgumentModel argumentModel1 = new ArgumentModel();
        argumentModel1.ios_value = "cthome_flight";
        argumentModel1.android_value="机票";
        argumentModel1.name="$FlightHome";
        argumentModel1.owner="James";
        argumentModels.add(argumentModel1);

        ArgumentModel argumentModel2 = new ArgumentModel();
        argumentModel2.ios_value = "CTLoadingAnimationView";
        argumentModel2.android_value="ProgressBar";
        argumentModel2.name="$ProgressBar";
        argumentModel2.owner="James";
        argumentModels.add(argumentModel2);

        ArgumentModel argumentModel3 = new ArgumentModel();
        argumentModel3.ios_value = "重试";
        argumentModel3.android_value="重试";
        argumentModel3.name="$Retry";
        argumentModel3.owner="James";
        argumentModels.add(argumentModel3);

        ArgumentModel argumentModel4 = new ArgumentModel();
        argumentModel4.ios_value = "抱歉";
        argumentModel4.android_value="抱歉";
        argumentModel4.name="$SorryLoadFail";
        argumentModel4.owner="James";
        argumentModels.add(argumentModel4);

        ArgumentModel argumentModel5 = new ArgumentModel();
        argumentModel5.ios_value = "知道了";
        argumentModel5.android_value="知道了";
        argumentModel5.name="$Know";
        argumentModel5.owner="James";
        argumentModels.add(argumentModel5);

        ArgumentModel argumentModel6 = new ArgumentModel();
        argumentModel6.ios_value = "国航CA1858 330大";
        argumentModel6.android_value="国航CA1858";
        argumentModel6.name="$RWFromFlightInfo";
        argumentModel6.owner="James";
        argumentModels.add(argumentModel6);

        ArgumentModel argumentModel7 = new ArgumentModel();
        argumentModel7.ios_value = "国航CA1831 33A大";
        argumentModel7.android_value="国航CA1831";
        argumentModel7.name="$RWBackFlightInfo";
        argumentModel7.owner="James";
        argumentModels.add(argumentModel7);

        ArgumentModel argumentModel8 = new ArgumentModel();
        argumentModel8.ios_value = "icon del2";
        argumentModel8.android_value="f3_button_person_list_select";
        argumentModel8.name="$DeletePerson";
        argumentModel8.owner="James";
        argumentModels.add(argumentModel8);

        ArgumentModel argumentModel9 = new ArgumentModel();
        argumentModel9.ios_value = "btn flightadd";
        argumentModel9.android_value="f3_boarding_person";
        argumentModel9.name="$AddPerson";
        argumentModel9.owner="James";
        argumentModels.add(argumentModel9);

        ArgumentModel argumentModel10 = new ArgumentModel();
        argumentModel10.ios_value = "UITextField";
        argumentModel10.android_value="EditText";
        argumentModel10.name="$ContactPhone";
        argumentModel10.owner="James";
        argumentModels.add(argumentModel10);

        ArgumentModel argumentModel11 = new ArgumentModel();
        argumentModel11.ios_value = "CTToastTipView";
        argumentModel11.android_value="toast_message";
        argumentModel11.name="$ToastView";
        argumentModel11.owner="James";
        argumentModels.add(argumentModel11);

        ArgumentModel argumentModel12 = new ArgumentModel();
        argumentModel12.ios_value = "签名末尾处最后3位";
        argumentModel12.android_value="签名栏末尾最后3位";
        argumentModel12.name="$CVV2";
        argumentModel12.owner="James";
        argumentModels.add(argumentModel12);

        ArgumentModel argumentModel13 = new ArgumentModel();
        argumentModel13.ios_value = "继续预订";
        argumentModel13.android_value="继续预订";
        argumentModel13.name="$RepeatOrder";
        argumentModel13.owner="James";
        argumentModels.add(argumentModel13);

        insertArgument(argumentModels);
    }
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
    public static String getArgumentRealValue(String argumentName,String os,String owner){
        if(argumentHashMap.isEmpty()){
           loadALLArgument();
        }
        HashMap<String,ArgumentModel> valueHashMap = argumentHashMap.get(owner);
        if(valueHashMap!=null){
            ArgumentModel model =  valueHashMap.get(argumentName);
            if(model!=null){
                return model.getValue(os);
            }
        }
        valueHashMap = argumentHashMap.get("all");
        if(valueHashMap!=null){
            ArgumentModel model =  valueHashMap.get(argumentName);
            if(model!=null){
                return model.getValue(os);
            }
        }
        return "";
    }
    public static void insertArgument(ArrayList<ArgumentModel> argumentModels){
        if(argumentModels!=null){
            ConnectionPool.PooledConnection conn = CustomDBManager.getConnection();
            try {
                StringBuilder listInsert = new StringBuilder();
                listInsert.append("INSERT INTO CaseArgumentTable (argument_name,android_value,ios_value,argument_owner) VALUES ");
                int count = argumentModels.size();
                for (int index = 0; index < count; index++) {
                    ArgumentModel argumentModel = argumentModels.get(index);
                    listInsert.append("('"+argumentModel.name+"','"+argumentModel.android_value+"','"+argumentModel.ios_value+"','"+argumentModel.owner+"')");
                    if (index != count - 1) {
                        listInsert.append(",");
                    }
                }
                listInsert.append(";");
                conn.executeUpdate(listInsert.toString());
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
