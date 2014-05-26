package android.ui.auto.framework;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.ui.auto.framework.command.AndroidActionCommandType;

public class AssetModel extends TestCaseNode {
	public String[] actions;
	public ArrayList<TestCaseNode> caseNodes = new ArrayList<TestCaseNode>();
	public String errorStep = "error";
	// public int currentOffset = -1;
	public TestCase testCase;
	public long startTime = 0;

	public AssetModel(TestCase testCase, String asset) {
		super(testCase);
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
				caseNode.actionStr = actionStr;
				caseNode.arg = argStr[0];
				caseNode.args = argStr;
				caseNodes.add(caseNode);
			} else {
				errorStep = action.trim().replace("goto:", "");
			}
		}
	}

	public void goToFail() {
		testCase.caseStepArray.add(testCase.getStep(errorStep));
	}

	// public TestCaseNode getNext() {
	// currentOffset = currentOffset + 1;
	// if (currentOffset < caseNodes.size()) {
	// TestCaseNode caseNode = caseNodes.get(currentOffset);
	// return caseNode;
	// }
	// return null;
	// }

	public TestCaseNode getNext() {
		TestCaseNode caseNode = new TestCaseNode(testCase);
		caseNode.action = AndroidActionCommandType.VIEWDUMP;
		caseNode.actionStr = "view dump";
		startTime = System.currentTimeMillis();
		return caseNode;
	}

	// public TestCaseNode getCurrent() {
	// if (currentOffset < caseNodes.size()) {
	// TestCaseNode caseNode = caseNodes.get(currentOffset);
	// return caseNode;
	// }
	// return null;
	// }
	//
	// public void goToSuccess() {
	// getCurrent().assetSuccess();
	// currentOffset = -1;
	// }

	public AssetModel cloneAsset(TestCase cloneCase) {
		AssetModel assetModel = new AssetModel(cloneCase, "");
		assetModel.actions = actions;
		assetModel.caseNodes = new ArrayList<TestCaseNode>();
		for (TestCaseNode caseNode : caseNodes) {
			assetModel.caseNodes.add(caseNode.cloneNode(cloneCase));
		}
		assetModel.errorStep = errorStep;

		return assetModel;
	}

	public TestCaseNode asset(String body) {
		JSONObject jsonObject = new JSONObject(body);
		if(jsonObject!=null){
			JSONArray jsonArray = jsonObject.optJSONArray("windows");
			if (jsonArray != null) {
				for (TestCaseNode caseNode : caseNodes) {
					if (isContainValue(jsonArray, caseNode)) {
						testCase.caseStepArray.add(testCase.getStep(caseNode.args[1]));
						if (caseNode.args[1].equals("waitProcess")) {
							testCase.caseStepArray.add(testCase.currentStep);
						}
						return null;
					}
				}
			}
		}
		return assetEmpty();
	}

	public TestCaseNode assetEmpty() {
		if (System.currentTimeMillis() - startTime >= AutoServer.TIMEOUT * 1000) {
			goToFail();
			return null;
		} else {
			TestCaseNode caseNode = new TestCaseNode(testCase);
			caseNode.action = AndroidActionCommandType.VIEWDUMP;
			caseNode.actionStr = "view dump";
			return caseNode;
		}
	}

	public boolean isContainValue(JSONArray jsonArray, TestCaseNode caseNode) {

		return false;
	}
}
