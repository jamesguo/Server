package auto.server.core;


import auto.server.bean.TestCase;
import auto.server.config.ServerConfig;
import plugin.sql.bean.BatchModel;
import plugin.sql.bean.TestCaseModel;
import plugin.sql.manager.BatchManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class AutoServer {
    public static boolean allCaseFinished = false;
    public static ArrayList<TestCase> testCases = new ArrayList<TestCase>();
    public static ArrayList<TestCase> runningTest = new ArrayList<TestCase>();
    public static ArrayList<BatchModel> batchModels = new ArrayList<BatchModel>();
    public static HashMap<String ,ArrayList<TestCase>> caseInfo = new HashMap<String ,ArrayList<TestCase>>();
    public static HashMap<String ,ArrayList<TestCase>> caseRunnindInfo = new HashMap<String ,ArrayList<TestCase>>();
    public static long TIMEOUT = 6;

    public AutoServerSocket server;

    public static void main(String[] args) {
        loadAllTestCase();
        int port = 6100;
        try {
            port = Integer.valueOf(ServerConfig.getConfig("ServerPort"));
        } catch (Exception e) {
            // TODO: handle exception
        }
        final int realPort = port;
        AutoServer autoServer = new AutoServer();
        try {
            autoServer.server = new AutoServerSocket(realPort);
            autoServer.server.startListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static synchronized TestCase getReadyTestCase(String url) {
        if(!caseInfo.containsKey(url)){
            ArrayList<TestCase> cases = new ArrayList<TestCase>();
            cases.addAll(testCases);
            caseInfo.put(url,cases);
            caseRunnindInfo.put(url,new ArrayList<TestCase>());
        }
        ArrayList<TestCase> allCases  =  caseInfo.get(url);
        if (allCases.size() > 0) {
//            TestCase testCase = allCases.get(0).cloneCase();
            TestCase testCase = allCases.remove(0);
            caseRunnindInfo.get(url).add(testCase);
            return testCase;
        }
        return null;
    }
    public static synchronized TestCase getReadyTestCase() {
        if (testCases.size() > 0) {
            TestCase testCase = testCases.get(0).cloneCase();
            // TestCase testCase = testCases.remove(0);
            // runningTest.add(testCase);

            return testCase;
        }
        return null;
    }

    public static void loadAllTestCase() {
        batchModels = BatchManager.getBatchList();
        for (BatchModel batchModel:batchModels){
            Collection<TestCaseModel> testCaseModels = batchModel.testCaseModels.values();
            for (TestCaseModel testCaseModel:testCaseModels){
                TestCase testCase = new TestCase(batchModel,testCaseModel);
                testCases.add(testCase);
            }
        }
    }

    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 获取指定目录下的所有的文件（不包括文件夹），采用了递归
     *
     * @param directory
     * @return
     */
    public static ArrayList<File> getListFiles(File directory) {
        ArrayList<File> files = new ArrayList<File>();
        if (directory.isFile()) {
            files.add(directory);
            return files;
        } else if (directory.isDirectory()) {
            File[] fileArr = directory.listFiles();
            for (int i = 0; i < fileArr.length; i++) {
                File fileOne = fileArr[i];
                files.addAll(getListFiles(fileOne));
            }
        }
        return files;
    }
}
