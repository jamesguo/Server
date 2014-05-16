package android.ui.auto.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Properties;

public class AutoServer {
	public static boolean allCaseFinished = false;
	public static ArrayList<TestCase> testCases = new ArrayList<TestCase>();
	public static ArrayList<TestCase> runningTest = new ArrayList<TestCase>();

	public static void main(String[] args) {
		loadAllTestCase();
		AutoServerSocket server;
		try {
			int port = 6100;
			try {
				port = Integer.valueOf(TestServerConfig.getConfig("ServerPort"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			server = new AutoServerSocket(port);
			server.startListener();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static synchronized TestCase getReadyTestCase() {
		if(testCases.size()>0){
			// TestCase testCase = testCases.get(1);
			TestCase testCase = testCases.remove(0);
			runningTest.add(testCase);

			return testCase;
		}
		return null;
	}
	public static void loadAllTestCase() {
		String testCasePath = TestServerConfig.getConfig("TestCasesPath");
		if (testCasePath != null) {
			File fileDir = new File(testCasePath);
			if (fileDir.exists()) {
				ArrayList<File> arrayList = getListFiles(fileDir);
				for (File file : arrayList) {
					if (getFileNameNoEx(file.getName()).equals("全局变量")) {
						if (GlobalContent.properties == null) {
							GlobalContent.properties = new Properties();
							try {
								InputStream inputStream = new FileInputStream(file);
								GlobalContent.properties.load(inputStream);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else {
						TestCase testCase = new TestCase(getFileNameNoEx(file.getName()));
						try {
							if (testCase.properties == null) {
								testCase.properties = new Properties();
								try {
									InputStream inputStream = new FileInputStream(file);
									testCase.properties.load(inputStream);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							testCase.initAllTestSetps();
							testCases.add(testCase);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
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

	/***
	 * 获取指定目录下的所有的文件（不包括文件夹），采用了递归
	 * 
	 * @param obj
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
