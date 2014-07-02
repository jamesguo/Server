package plugin.sql.bean;

import auto.server.log.LogUtil;
import auto.server.util.TypeConvertUtil;

import java.sql.Date;

/**
 * Created by yrguo on 14-5-30.
 */
public class ExcuteResultModel {
    public enum Excute_State{
        INIT(0,"初始化"),
        RUNNING(1,"运行中"),
        SUCCESS(2,"成功"),
        FAIL(3,"失败");
        public final int code;
        public final String name;
        private Excute_State(int code,String name){
            this.code=code;
            this.name=name;
        }
    }
    public int batch_number = 0;
    public int case_number = 0;
    public String device_name = "";
    public Excute_State case_excute_state=Excute_State.INIT;
    public String case_result_path = "";
    public String case_start_time = TypeConvertUtil.getFormatTime() ;
    public String case_end_time = TypeConvertUtil.getFormatTime();
}

