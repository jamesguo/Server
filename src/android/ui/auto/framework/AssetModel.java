package android.ui.auto.framework;

import android.ui.auto.framework.command.AndroidActionCommandType;
import android.ui.auto.framework.log.LogUtil;
import android.ui.auto.framework.util.TypeConvertUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        strValue = "asset";
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
                caseNode.strValue = action;
                caseNode.arg = argStr[0];
                caseNode.args = argStr;
                caseNodes.add(caseNode);
            } else {
                errorStep = action.trim().replace("goto:", "");
            }
        }
    }

    public void goToFail() {
        LogUtil.debug(testCase, "[" + testCase.name + "]" + "全部验证失败");
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
        caseNode.strValue = "asset";
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
        if (jsonObject != null) {
            String windows = jsonObject.optString("windows");
            JSONArray jsonArray = new JSONArray(windows);
            if (jsonArray != null) {
                ArrayList<String> keys = new ArrayList<String>();
                for (TestCaseNode caseNode : caseNodes) {
                    if (caseNode.arg.equals("")) {
                        testCase.caseStepArray.add(testCase.getStep(caseNode.args[1].trim().replace("goto:", "")));
                        return null;
                    }
                    if (caseNode.arg.startsWith("$")) {
                        caseNode.arg = GlobalContent.getConfig(caseNode.arg, testCase.deviceOS);
                    }
                    keys.add(TypeConvertUtil.getSimpleStr(caseNode.arg));
                }
                int index = isContainValue(jsonArray, keys);
                if (index != -1 && index < caseNodes.size()) {
                    TestCaseNode caseNode = caseNodes.get(index);
                    testCase.caseStepArray.add(testCase.getStep(caseNode.args[1].trim().replace("goto:", "")));
                    if (caseNode.args[1].trim().replace("goto:", "").equals("waitProcess")) {
                        testCase.caseStepArray.add(testCase.currentStep);
                    }
                    return null;
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
            try {
                Thread.sleep(250);
            } catch (Exception e) {
                // TODO: handle exception
            }
            TestCaseNode caseNode = new TestCaseNode(testCase);
            caseNode.action = AndroidActionCommandType.VIEWDUMP;
            caseNode.actionStr = "view dump";
            caseNode.strValue = "asset";
            return caseNode;
        }
    }

    public int isContainValue(JSONArray jsonArray, ArrayList<String> keys) {
        int count = jsonArray.length();
        System.out.println(jsonArray.toString());
        for (int index = 0; index < count; index++) {
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            String classAllInfo = jsonObject.optString("class", "");
            String className = classAllInfo.substring(classAllInfo.indexOf(":") + 1);
            if (keys.indexOf(className) >= 0) {
                return keys.indexOf(className);
            }
            String otherInfo = classAllInfo.substring(0, classAllInfo.indexOf(":"));
            if (otherInfo.length() > 0) {
                String[] infos = otherInfo.split("\\|");
                List<String> list = (List<String>) Arrays.asList(infos);
                ArrayList<String> arrayList = new ArrayList<String>();
                arrayList.addAll(list);
                for (int i = 0; i < keys.size(); i++) {
                    if (arrayList.contains(keys.get(i))) {
                        return i;
                    }
                }
//					else {
//						JSONArray viewDescriptionArray = jsonObject.optJSONArray("props");
//						if (viewDescriptionArray != null && viewDescriptionArray.length() > 0) {
//							int size = viewDescriptionArray.length();
//							for (int position = 0; position < size; position++) {
//								JSONObject object = viewDescriptionArray.getJSONObject(position);
//								if (object != null) {
//									if (object.opt("value") != null && caseNode.arg.equals("" + object.opt("value"))) {
//										return true;
//									}
//								}
//							}
//						}
//					}
            }

            JSONArray childViews = jsonObject.optJSONArray("views");
            if (childViews != null) {
                int position = isContainValue(childViews, keys);
                if (position != -1 && position < keys.size()) {
                    return position;
                }
            }
        }
        return -1;
    }
}
