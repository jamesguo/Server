package android.ui.auto.framework;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import android.ui.auto.framework.command.AndroidActionCommand;
import android.ui.auto.framework.command.AndroidActionCommandType;
import android.ui.auto.framework.log.LogUtil;

public class TestCase {
	public String name;
	public String outPath;
	public String deviceName = "";
	public String deviceOS = "";
	public Properties properties;
	public LinkedBlockingQueue<TestCaseStep> caseStepArray = new LinkedBlockingQueue<TestCaseStep>();
	public HashMap<String, TestCaseStep> testSteps = new HashMap<String, TestCaseStep>();
	public TestCaseStep currentStep;

	public TestCase(String name) {
		this.name = name;
		outPath = TestServerConfig.getConfig("ResultOutPath") + File.separatorChar + name;
		File file = new File(outPath);
		file.mkdirs();
	}

	public synchronized void startCase() {
		TestCaseStep caseStep = getStep("step1");
		if (caseStep != null) {
			caseStepArray.add(caseStep);
		}
	}

	public String getProperty(String key) {
		String value = properties.getProperty(key, "");
		String str = null;
		try {
			// 进行编码转换，解决问题
			str = new String(value.getBytes("ISO8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	public void initAllTestSetps() {
		if (properties != null && !properties.isEmpty()) {
			Set<Object> keys = properties.keySet();
			for (Object key : keys) {
				if (key instanceof String) {
					if (((String) key).startsWith("step")) {
						String actionKey = ((String) key);
						String assetKey = actionKey.replace("step", "asset");
						String errorKey = actionKey.replace("step", "error");
						String limitKey = actionKey.replace("step", "limit");
						String actionStr = getProperty(actionKey).trim();
						String asset1 = getProperty(assetKey).trim();
						String error1 = getProperty(errorKey).trim();
						String limit = getProperty(limitKey).trim();
						if (actionStr != null && !actionStr.isEmpty()) {
							TestCaseStep caseStep = new TestCaseStep(this);
							caseStep.name = actionKey;
							String[] actions = actionStr.split(";");
							for (String action : actions) {
								String temp = action.trim();
								if (temp.length() > 0) {
									TestCaseNode caseNode = new TestCaseNode(this);
									String actionName = temp.substring(0, temp.indexOf("("));
									String args = temp.substring(temp.indexOf("(") + 1, temp.indexOf(")"));
									caseNode.actionStr = actionName;
									caseNode.action = AndroidActionCommandType.getActionFromStr(actionName);
									caseNode.arg = args;
									caseStep.actions.add(caseNode);
								}
							}
							if (asset1 != null && !asset1.isEmpty()) {
								caseStep.assetModel = new AssetModel(this, asset1);
							}
							if (error1 != null && !error1.isEmpty()) {
								caseStep.errorModel = new ErrorModel(this, error1);
							}
							if (limit != null && !limit.isEmpty()) {
								try {
									caseStep.limitTime = Integer.valueOf(limit);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							testSteps.put(actionKey, caseStep);
						}
					} else if (((String) key).equals("success")) {
						String actionStr = getProperty("success").trim();
						TestCaseStep caseStep = new TestCaseStep(this);
						caseStep.name = "success";
						String[] actions = actionStr.split(";");
						for (String action : actions) {
							String temp = action.trim();
							TestCaseNode caseNode = new TestCaseNode(this);
							String actionName = temp.substring(0, temp.indexOf("("));
							String args = temp.substring(temp.indexOf("(") + 1, temp.indexOf(")"));
							caseNode.action = AndroidActionCommandType.getActionFromStr(actionName);
							caseNode.actionStr = actionName;
							caseNode.arg = args;
							caseStep.actions.add(caseNode);
						}
						testSteps.put("success", caseStep);
					} else if (((String) key).equals("error")) {
						String actionStr = getProperty("error").trim();
						TestCaseStep caseStep = new TestCaseStep(this);
						caseStep.name = "error";
						String[] actions = actionStr.split(";");
						for (String action : actions) {
							String temp = action.trim();
							TestCaseNode caseNode = new TestCaseNode(this);
							String actionName = temp.substring(0, temp.indexOf("("));
							String args = temp.substring(temp.indexOf("(") + 1, temp.indexOf(")"));
							caseNode.action = AndroidActionCommandType.getActionFromStr(actionName);
							caseNode.actionStr = actionName;
							caseNode.arg = args;
							caseStep.actions.add(caseNode);
						}
						testSteps.put("error", caseStep);
					} else if (((String) key).equals("waitProcess")) {
						String actionStr = getProperty("waitProcess").trim();
						TestCaseStep caseStep = new TestCaseStep(this);
						caseStep.name = "waitProcess";
						caseStep.limitTime = Integer.MAX_VALUE;
						String[] actions = actionStr.split(";");
						for (String action : actions) {
							String temp = action.trim();
							TestCaseNode caseNode = new TestCaseNode(this);
							String actionName = temp.substring(0, temp.indexOf("("));
							String args = temp.substring(temp.indexOf("(") + 1, temp.indexOf(")"));
							caseNode.action = AndroidActionCommandType.getActionFromStr(actionName);
							caseNode.actionStr = actionName;
							caseNode.arg = args;
							caseStep.actions.add(caseNode);
						}
						testSteps.put("waitProcess", caseStep);
					}
				}
			}
		} else {
			System.out.println("Case 解析失败");
		}
	}

	public AndroidActionCommand runNextNode(AndroidActionCommand cmd) {
		if (currentStep == null) {
			currentStep = caseStepArray.poll();
		}
		AndroidActionCommand actionCommand = currentStep.runNextNode(cmd);
		if (actionCommand == null) {
			try {
				TestCaseStep lastCaseStep = currentStep;
				currentStep = caseStepArray.poll(3, TimeUnit.SECONDS);
				while (currentStep.limitTime <= currentStep.excuteTime) {
					currentStep.assetModel.goToFail();
					currentStep = caseStepArray.poll(3, TimeUnit.SECONDS);
				}
				if (currentStep == null) {
					if (lastCaseStep.name.equals("success") || lastCaseStep.name.equals("error")) {
						LogUtil.error(this, "Test Case going to finish");
					} else {
						currentStep = getStep("error");
					}
				} else {
					if (lastCaseStep.name.equals("waitProcess")) {
						AndroidActionCommand androidActionCommand = new AndroidActionCommand();
						androidActionCommand.result = 1;
						actionCommand = currentStep.runNextNode(androidActionCommand);
					} else {
						actionCommand = currentStep.runNextNode(new AndroidActionCommand());
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return actionCommand;
	}

	public synchronized void reset(boolean start) {
		if (start) {
			LogUtil.error(this, "开始 测试 case状态回至");
		} else {
			LogUtil.error(this, "结束 测试 case状态回至");
		}
		caseStepArray.clear();
		deviceName = "";
		deviceOS = "";
		currentStep = null;
		for (TestCaseStep caseStep : testSteps.values()) {
			caseStep.excuteTime = 0;
			caseStep.currentAction = -1;
			if (caseStep.assetModel != null) {
				caseStep.assetModel.startTime = 0;
			}
		}
	}

	public TestCaseStep getStep(String stepName) {
		// TODO Auto-generated method stub
		TestCaseStep caseStep = testSteps.get(stepName);
		if (caseStep == null) {
			caseStep = testSteps.get(stepName + "_" + deviceOS);
		}
		return caseStep;
	}

	public TestCase cloneCase() {
		TestCase cloneCase = new TestCase(name);
		cloneCase.outPath = outPath;
		cloneCase.deviceName = deviceName;
		cloneCase.deviceOS = deviceOS;
		cloneCase.properties = properties;
		cloneCase.caseStepArray = new LinkedBlockingQueue<TestCaseStep>();
		cloneCase.testSteps = new HashMap<String, TestCaseStep>();
		Set<String> keys = testSteps.keySet();
		for (String key : keys) {
			TestCaseStep caseStep = testSteps.get(key);
			TestCaseStep cloneStep = caseStep.cloneStep(cloneCase);
			cloneCase.testSteps.put(key, cloneStep);
		}
		if (currentStep != null) {
			cloneCase.currentStep = currentStep.cloneStep(cloneCase);
		} else {
			cloneCase.currentStep = null;
		}
		return cloneCase;
	}
}
