package android.ui.auto.framework.log;

import android.ui.auto.framework.TestCase;

import java.io.*;
import java.text.SimpleDateFormat;

public class LogUtil {
    private static String prefix = " [UIAUTO]";
    private static String suffix = "[/UIAUTO]";

    public static void info(TestCase testCase, String string) {
        String log = getFormatTime() + prefix + " [info] " + string + suffix;
        System.out.println(log);
        writeToFile(testCase, log);
    }

    public static void error(TestCase testCase, String string) {
        String log = getFormatTime() + prefix + " [error] " + string + suffix;
        System.out.println(log);
        writeToFile(testCase, log);
    }

    public static void debug(TestCase testCase, String string) {
        String log = getFormatTime() + prefix + " [debug] " + string + suffix;
        System.out.println(log);
        writeToFile(testCase, log);
    }

    public static void writeToFile(TestCase testCase, String string) {
        if (testCase != null && !testCase.deviceName.equals("")) {
            File fileDir = new File(testCase.outPath + File.separatorChar + testCase.deviceName + File.separatorChar + testCase.identify);
            fileDir.mkdirs();
            File logInfo = new File(testCase.outPath + File.separatorChar + testCase.deviceName + File.separatorChar + testCase.identify + File.separatorChar + "logInfo.txt");
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logInfo, true), "UTF-8"));
                out.write(string + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getFormatTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
    }

}
