package plugin.sql.manager;

import android.ui.auto.framework.TestCaseStep;
import plugin.sql.CustomDBManager;
import plugin.sql.bean.TestCaseModel;
import plugin.sql.bean.TestCaseStepModel;
import plugin.sql.util.ConnectionPool;

import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by yrguo on 14-5-29.
 */
public class CaseListManager {
    public static void main(String[] args) {
        TestCaseModel testCaseModel = new TestCaseModel();
        testCaseModel.owner = "James";
        testCaseModel.caseName = "国";
        ArrayList<TestCaseStepModel> arrayList = new ArrayList<TestCaseStepModel>();
        for (int i = 1; i < 6; i++) {
            TestCaseStepModel testCaseStepModel = new TestCaseStepModel();
            testCaseStepModel.step_name = "step" + i;
            testCaseStepModel.step_action = "find();";
            testCaseStepModel.step_asset = "see";
            testCaseStepModel.step_error = "goto";
            testCaseStepModel.step_limit = 50;
            arrayList.add(testCaseStepModel);
        }
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
