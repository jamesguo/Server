package plugin.sql.manager;

import plugin.sql.CustomDBManager;
import plugin.sql.bean.TestCaseModel;
import plugin.sql.bean.TestCaseStepModel;
import plugin.sql.util.ConnectionPool;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by yrguo on 14-5-29.
 */
public class CaseListManager {
    public static void main(String[] args) {
        TestCaseModel testCaseModel = new TestCaseModel();
        testCaseModel.owner = "James";
        testCaseModel.caseName = "国内往返";
        ArrayList<TestCaseStepModel> arrayList = new ArrayList<TestCaseStepModel>();
        TestCaseStepModel testCaseStepModel0 = new TestCaseStepModel();
        testCaseStepModel0.step_name = "start";
        testCaseStepModel0.step_action = "wait(5);DeviceInfo();";
        testCaseStepModel0.step_asset = "see($FlightHome,goto:step2);goto:error";
        testCaseStepModel0.step_error = "goto:error;";
        arrayList.add(testCaseStepModel0);


        TestCaseStepModel testCaseStepModel1 = new TestCaseStepModel();
        testCaseStepModel1.step_name = "step2";
        testCaseStepModel1.step_action = "find($FlightHome);click()";
        testCaseStepModel1.step_asset = "see(查询,goto:step3);goto:error";
        testCaseStepModel1.step_error = "goto:error;";
        arrayList.add(testCaseStepModel1);

        TestCaseStepModel testCaseStepModel2 = new TestCaseStepModel();
        testCaseStepModel2.step_name = "step3";
        testCaseStepModel2.step_action = "screenshot(查询页.jpg);find(往返);click();find(查询);click();";
        testCaseStepModel2.step_asset = "seeView($ProgressBar,goto:waitProcess);see($RWFromFlightInfo,goto:step5);see($Retry,goto:step4);goto:error";
        testCaseStepModel2.step_error = "goto:error;";
        arrayList.add(testCaseStepModel2);

        TestCaseStepModel testCaseStepModel3 = new TestCaseStepModel();
        testCaseStepModel3.step_name = "step4";
        testCaseStepModel3.step_action = "find($Retry);click();";
        testCaseStepModel3.step_asset = "seeView($ProgressBar,goto:waitProcess);see($RWFromFlightInfo,goto:step5);see($Retry,goto:step4);goto:error";
        testCaseStepModel3.step_error = "goto:error;";
        testCaseStepModel3.step_limit = 3;
        arrayList.add(testCaseStepModel3);

        TestCaseStepModel testCaseStepModel4 = new TestCaseStepModel();
        testCaseStepModel4.step_name = "step5";
        testCaseStepModel4.step_action = "find($RWFromFlightInfo);click();";
        testCaseStepModel4.step_asset = "seeView($ProgressBar,goto:waitProcess);see(选定,goto:step7);see($Know,step6);goto:error";
        testCaseStepModel4.step_error = "goto:error;";
        arrayList.add(testCaseStepModel4);

        TestCaseStepModel testCaseStepModel5 = new TestCaseStepModel();
        testCaseStepModel5.step_name = "step6";
        testCaseStepModel5.step_action = "find($Know);click();";
        testCaseStepModel5.step_asset = "see(,goto:step5);goto:error";
        testCaseStepModel5.step_error = "goto:error;";
        testCaseStepModel5.step_limit = 3;
        arrayList.add(testCaseStepModel5);

        TestCaseStepModel testCaseStepModel6 = new TestCaseStepModel();
        testCaseStepModel6.step_name = "step7";
        testCaseStepModel6.step_action = "find(选定);click();";
        testCaseStepModel6.step_asset = "seeView($ProgressBar,goto:waitProcess);see($RWBackFlightInfo,goto:step10);see($Retry,goto:step9);goto:error";
        testCaseStepModel6.step_error = "goto:error;";
        arrayList.add(testCaseStepModel6);

        TestCaseStepModel testCaseStepModel7 = new TestCaseStepModel();
        testCaseStepModel7.step_name = "step9";
        testCaseStepModel7.step_action = "find($Retry);click();";
        testCaseStepModel7.step_asset = "seeView($ProgressBar,goto:waitProcess);see($RWBackFlightInfo,goto:step5);see($Retry,goto:step9);goto:error";
        testCaseStepModel7.step_error = "goto:error;";
        testCaseStepModel7.step_limit = 3;
        arrayList.add(testCaseStepModel7);

        TestCaseStepModel testCaseStepModel8 = new TestCaseStepModel();
        testCaseStepModel8.step_name = "step10";
        testCaseStepModel8.step_action = "find($RWBackFlightInfo);click();";
        testCaseStepModel8.step_asset = "seeView($ProgressBar,goto:waitProcess);see(预订,goto:step12);see($Know,step11);goto:error";
        testCaseStepModel8.step_error = "goto:error;";
        arrayList.add(testCaseStepModel8);

        TestCaseStepModel testCaseStepModel9 = new TestCaseStepModel();
        testCaseStepModel9.step_name = "step11";
        testCaseStepModel9.step_action = "find($Know);click();";
        testCaseStepModel9.step_asset = "see(,goto:step10);goto:error";
        testCaseStepModel9.step_error = "goto:error;";
        testCaseStepModel9.step_limit = 3;
        arrayList.add(testCaseStepModel9);

        TestCaseStepModel testCaseStepModel10 = new TestCaseStepModel();
        testCaseStepModel10.step_name = "step12";
        testCaseStepModel10.step_action = "find(预订);click();";
        testCaseStepModel10.step_asset = "seeView($ProgressBar,goto:waitProcess);see($Know,goto:step13);see($DeletePerson,goto:step14);goto:step15";
        testCaseStepModel10.step_error = "goto:error;";
        arrayList.add(testCaseStepModel10);

        TestCaseStepModel testCaseStepModel11 = new TestCaseStepModel();
        testCaseStepModel11.step_name = "step13";
        testCaseStepModel11.step_action = "find($Know);click();";
        testCaseStepModel11.step_asset = "see(,goto:step12);goto:error";
        testCaseStepModel11.step_error = "goto:error;";
        testCaseStepModel11.step_limit = 3;
        arrayList.add(testCaseStepModel11);

        TestCaseStepModel testCaseStepModel12 = new TestCaseStepModel();
        testCaseStepModel12.step_name = "step14_Android";
        testCaseStepModel12.step_action = "find($DeletePerson);click();";
        testCaseStepModel12.step_asset = "see($DeletePerson,goto:step14);goto:step15";
        testCaseStepModel12.step_error = "goto:step15;";
        arrayList.add(testCaseStepModel12);

        TestCaseStepModel testCaseStepModel13 = new TestCaseStepModel();
        testCaseStepModel13.step_name = "step14_IOS";
        testCaseStepModel13.step_action = "find($DeletePerson);click();find(删除);click();";
        testCaseStepModel13.step_asset = "see($DeletePerson,goto:step14);goto:step15";
        testCaseStepModel13.step_error = "goto:step15;";
        arrayList.add(testCaseStepModel13);

        TestCaseStepModel testCaseStepModel14 = new TestCaseStepModel();
        testCaseStepModel14.step_name = "step15";
        testCaseStepModel14.step_action = "find($AddPerson);click();find(小辛xiao/xin);click();find(完成);click();";
        testCaseStepModel14.step_asset = "see(联系手机,goto:step16);goto:error";
        testCaseStepModel14.step_error = "goto:error;";
        arrayList.add(testCaseStepModel14);

        TestCaseStepModel testCaseStepModel15 = new TestCaseStepModel();
        testCaseStepModel15.step_name = "step16";
        testCaseStepModel15.step_action = "findView($ContactPhone);presskey(13518336231);";
        testCaseStepModel15.step_asset = "see(下一步,goto:step17);goto:error";
        testCaseStepModel15.step_error = "goto:error;";
        arrayList.add(testCaseStepModel15);

        TestCaseStepModel testCaseStepModel16 = new TestCaseStepModel();
        testCaseStepModel16.step_name = "step17";
        testCaseStepModel16.step_action = "find(下一步);click();";
        testCaseStepModel16.step_asset = "seeView($ProgressBar,goto:waitProcess);see(支付方式,goto:step18);see($RepeatOrder,goto:step19);see($Know,goto:error);goto:error";
        testCaseStepModel16.step_error = "goto:error;";
        arrayList.add(testCaseStepModel16);

        TestCaseStepModel testCaseStepModel17 = new TestCaseStepModel();
        testCaseStepModel17.step_name = "step18";
        testCaseStepModel17.step_action = "find(常用卡支付);click();find(民生银行-信用卡);click();find(请输入卡号后四位);presskey(0008);find($CVV2);presskey(321);find(支付);click();";
        testCaseStepModel17.step_asset = "seeView($ProgressBar,goto:waitProcess);seeView($ToastView,goto:step18);see(订单结果,goto:success);goto:error";
        testCaseStepModel17.step_error = "goto:error;";
        testCaseStepModel17.step_limit = 3;
        arrayList.add(testCaseStepModel17);

        TestCaseStepModel testCaseStepModel18 = new TestCaseStepModel();
        testCaseStepModel18.step_name = "step19";
        testCaseStepModel18.step_action = "find($RepeatOrder);click();";
        testCaseStepModel18.step_asset = "see(,goto:step18);goto:step18";
        testCaseStepModel18.step_error = "goto:step18";
        arrayList.add(testCaseStepModel18);

        TestCaseStepModel testCaseStepModel19 = new TestCaseStepModel();
        testCaseStepModel19.step_name = "waitProcess";
        testCaseStepModel19.step_action = "findView($ProgressBar);WaitToDisappear()";
        arrayList.add(testCaseStepModel19);

        TestCaseStepModel testCaseStepModel20 = new TestCaseStepModel();
        testCaseStepModel20.step_name = "success";
        testCaseStepModel20.step_action = "screenshot(success.jpg);finish()";
        arrayList.add(testCaseStepModel20);

        TestCaseStepModel testCaseStepModel21 = new TestCaseStepModel();
        testCaseStepModel21.step_name = "error";
        testCaseStepModel21.step_action = "screenshot(error.jpg);finish()";
        arrayList.add(testCaseStepModel21);



        testCaseModel.steps = arrayList;
        insertCase(testCaseModel);
    }

    /*
    *
    * 根据CaseID获取CaseModel
    * */
    public static TestCaseModel readCaseByID(int case_number) {
        ConnectionPool.PooledConnection conn = CustomDBManager.getConnection();
        StringBuilder readCaseBuild = new StringBuilder();
        readCaseBuild.append("SELECT CaseListTable.*,CaseStepListTable.* from CaseListTable INNER JOIN CaseStepListTable on CaseListTable.case_number = '" + case_number + "' and CaseStepListTable.case_number = '" + case_number + "'");
        try {
            ResultSet resultSet = conn.executeQuery(readCaseBuild.toString());
            TestCaseModel testCaseModel = null;
            while (resultSet.next()) {
                if (testCaseModel == null) {
                    testCaseModel = new TestCaseModel();
                    testCaseModel.case_number = case_number;
                    testCaseModel.owner = resultSet.getString("case_owner");
                    testCaseModel.caseName = resultSet.getString("case_name");
                }
                TestCaseStepModel stepModel = new TestCaseStepModel();
                stepModel.step_asset = resultSet.getString("step_asset");
                stepModel.step_action = resultSet.getString("step_action");
                stepModel.step_error = resultSet.getString("step_error");
                stepModel.step_limit = resultSet.getInt("step_asset");
                testCaseModel.steps.add(stepModel);
            }
            return testCaseModel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     *
     * 根据案例ID删除案例
     *
     * **/
    public static void deleteCase(int case_number){
        ConnectionPool.PooledConnection conn = CustomDBManager.getConnection();
        try{
            conn.executeUpdate("DELETE CaseListTable.*,CaseStepListTable.* From CaseListTable,CaseStepListTable WHERE CaseListTable.case_number = '"+case_number+"' AND CaseStepListTable.case_number = '"+case_number+"'");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            conn.close();
        }
    }
    /*
    *
    * 插入新的Case
    *
    * */
    public static void insertCase(TestCaseModel testCaseModel) {
        String sql = "";
        ConnectionPool.PooledConnection conn = CustomDBManager.getConnection();
        try {
            sql = "INSERT INTO CaseListTable (case_name,case_owner) VALUES ('" + testCaseModel.caseName + "','" + testCaseModel.owner + "');";
            conn.executeUpdate(sql);
            sql = "SELECT LAST_INSERT_ID();";
            ResultSet resultSet = conn.executeQuery(sql);
            if (resultSet.next()) {
                int case_number = resultSet.getInt(1);
                System.out.println("Case_name : " + case_number);
                StringBuilder listInsert = new StringBuilder();
                listInsert.append("INSERT INTO CaseStepListTable (case_number,step_name,step_action,step_asset,step_error,step_limit) VALUES ");
                int count = testCaseModel.steps.size();
                for (int index = 0; index < count; index++) {
                    TestCaseStepModel testStep = testCaseModel.steps.get(index);
                    listInsert.append("(" + case_number + ",'" + testStep.step_name + "','" + testStep.step_action + "','" + testStep.step_asset + "','" + testStep.step_error + "','" + testStep.step_limit + "')");
                    if (index != count - 1) {
                        listInsert.append(",");
                    }
                }
                listInsert.append(";");
                try {
                    conn.executeUpdate(listInsert.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    conn.executeUpdate("DELETE CaseListTable.*,CaseStepListTable.* From CaseListTable,CaseStepListTable WHERE CaseListTable.case_number = '"+case_number+"' AND CaseStepListTable.case_number = '"+case_number+"'");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }
}
