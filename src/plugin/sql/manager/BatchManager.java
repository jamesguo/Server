package plugin.sql.manager;

import plugin.sql.CustomDBManager;
import plugin.sql.bean.ArgumentModel;
import plugin.sql.bean.BatchModel;
import plugin.sql.bean.TestCaseModel;
import plugin.sql.bean.TestCaseStepModel;
import plugin.sql.util.ConnectionPool;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yrguo on 14-5-29.
 */
public class BatchManager {
    public static HashMap<Integer, BatchModel> batchModelHashMap = new HashMap<Integer, BatchModel>();

    public static void loadALLReadyBatch() {
        ConnectionPool.PooledConnection conn = CustomDBManager.getConnection();
        try {
            ResultSet resultSet = conn.executeQuery("SELECT BatchTable.*,BatchCaseMap.*,CaseListTable.*,CaseStepListTable.* FROM ((BatchTable INNER JOIN BatchCaseMap ON BatchTable.`batch_state` = '1' AND BatchTable.`batch_number` = BatchCaseMap.`batch_number`) INNER JOIN CaseListTable ON CaseListTable.`case_number` = BatchCaseMap.`case_number`) INNER JOIN CaseStepListTable ON CaseStepListTable.`case_number` = CaseListTable.`case_number`");
            while (resultSet.next()) {
                int batch_number = resultSet.getInt("batch_number");
                BatchModel batchModel = batchModelHashMap.get(batch_number);
                if (batchModel == null) {
                    batchModel = new BatchModel();
                    batchModel.batch_number = batch_number;
                    batchModel.batch_owner = resultSet.getString("batch_owner");
                    batchModel.batch_state = resultSet.getInt("batch_state");
                    batchModelHashMap.put(batch_number, batchModel);
                }
                int case_number = resultSet.getInt("case_number");
                TestCaseModel testCaseModel = batchModel.testCaseModels.get(case_number);
                if (testCaseModel == null) {
                    testCaseModel = new TestCaseModel();
                    testCaseModel.case_number = case_number;
                    testCaseModel.owner = resultSet.getString("case_owner");
                    testCaseModel.caseName = resultSet.getString("case_name");
                    batchModel.testCaseModels.put(case_number, testCaseModel);
                }
                TestCaseStepModel stepModel = new TestCaseStepModel();
                stepModel.step_name = resultSet.getString("step_name");
                stepModel.step_asset = resultSet.getString("step_asset");
                stepModel.step_action = resultSet.getString("step_action");
                stepModel.step_error = resultSet.getString("step_error");
                stepModel.step_limit = resultSet.getInt("step_limit");
                testCaseModel.steps.add(stepModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }

    public static void finishBatchModel(int batch_number) {
        ConnectionPool.PooledConnection conn = CustomDBManager.getConnection();
        try {
            conn.executeUpdate("UPDATE batchtable SET batch_state = '0' WHERE batch_number = '" + batch_number + "'';");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }

    public static ArrayList<BatchModel> getBatchList() {
        if (batchModelHashMap.isEmpty()) {
            loadALLReadyBatch();
            if (batchModelHashMap.isEmpty()) {
                return new ArrayList<BatchModel>();
            }
        }
        ArrayList<BatchModel> arrayList = new ArrayList<BatchModel>();
        arrayList.addAll(batchModelHashMap.values());
        return arrayList;
    }
}
