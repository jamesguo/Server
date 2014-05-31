package plugin.sql.bean;

import java.util.ArrayList;

/**
 * Created by James on 14-5-29.
 */
public class BatchModel {
    public String batch_owner="";
    public int batch_number = 0;
    public ArrayList<TestCaseModel> testCaseModels = new ArrayList<TestCaseModel>();
}
