package auto.server.util;

import plugin.sql.manager.ArgumentManager;

public class GlobalContent {
    public static String getConfig(String key, String os,String owner) {
       return ArgumentManager.getArgumentRealValue(key,os,owner);
    }
}
