package plugin.sql.bean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by James on 14-5-29.
 */
public class BatchModel {
    public String batch_owner="";
    public int batch_number = 0;
    public int batch_state = 0;
    public int batch_excuted_time = 0;
    public HashMap<Integer,TestCaseModel> testCaseModels = new HashMap<Integer,TestCaseModel>();
    public BatchModel(){

    }
}
