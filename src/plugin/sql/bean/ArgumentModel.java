package plugin.sql.bean;

/**
 * Created by yrguo on 14-5-29.
 */
public class ArgumentModel {
    public String name = "";
    public String android_value = "";
    public String ios_value = "";
    public String owner = "";

    public ArgumentModel() {

    }

    public String getValue(String os) {
        if(os.equalsIgnoreCase("Android")){
            return android_value;
        }else if (os.equalsIgnoreCase("IOS")){
            return ios_value;
        }
        return "";
    }

}