package plugin.sql.bean;

import java.util.ArrayList;

/**
 * Created by yrguo on 14-5-29.
 */
public class TestCaseModel {
    public String owner="";
    public String caseName="";
    public int case_number=0;
    public ArrayList<TestCaseStepModel> steps = new ArrayList<TestCaseStepModel>();
    public TestCaseModel(){

    }
}
