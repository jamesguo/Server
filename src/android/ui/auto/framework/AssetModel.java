package android.ui.auto.framework;

import java.util.ArrayList;

import android.ui.auto.framework.command.AndroidActionCommandType;

public class AssetModel {
	public String[] actions;
	public ArrayList<TestCaseNode> caseNodes = new ArrayList<TestCaseNode>();
	public String errorStep = "error";
	public int currentOffset = -1;
	public TestCase testCase;

	public AssetModel(TestCase testCase, String asset) {
		this.testCase = testCase;
		String[] actionTemps = asset.split(";");
		for (String action : actionTemps) {
			if (action.trim().startsWith("see")) {
				action = action.trim();
				TestCaseNode caseNode = new TestCaseNode(testCase);
				String actionStr = action.substring(0, action.indexOf("("));
				String argsStr = action.substring(action.indexOf("(") + 1, action.indexOf(")"));
				caseNode.action = AndroidActionCommandType.getActionFromStr(actionStr);
				String[] argStr = argsStr.split(",");
				caseNode.arg = argStr[0];
				caseNode.args = argStr;
				caseNodes.add(caseNode);
			} else {
				errorStep = action.trim().replace("goto:", "");
			}
		}
	}

	public void goToFail() {
		testCase.caseStepArray.add(testCase.testSteps.get(errorStep));
		currentOffset = -1;
	}

	public TestCaseNode getNext() {
		currentOffset = currentOffset + 1;
		if (currentOffset < caseNodes.size()) {
			TestCaseNode caseNode = caseNodes.get(currentOffset);
			return caseNode;
		}
		return null;
	}

	public TestCaseNode getCurrent() {
		if (currentOffset < caseNodes.size()) {
			TestCaseNode caseNode = caseNodes.get(currentOffset);
			return caseNode;
		}
		return null;
	}

	public void goToSuccess() {
		getCurrent().assetSuccess();
		currentOffset = -1;
	}
}
