package auto.server.bean;


import auto.server.command.AndroidActionCommand;
import auto.server.command.AndroidActionCommandType;
import auto.server.log.LogUtil;
import plugin.sql.bean.TestCaseStepModel;

import java.util.ArrayList;

public class TestCaseStep {
    public ArrayList<TestCaseNode> actions = new ArrayList<TestCaseNode>();
    public String name = "";
    public AssetModel assetModel;
    public ErrorModel errorModel;
    public int excuteTime = 0;
    public int limitTime = 20;
    public int currentAction = -1;
    public TestCase testCase;
    public TestCaseStepModel caseStepModel;

    public TestCaseStep(TestCase testCase, TestCaseStepModel caseStepModel) {
        this.testCase = testCase;
        this.caseStepModel = caseStepModel;
        this.name = caseStepModel.step_name;
        this.limitTime = caseStepModel.step_limit;
        String stepAction = caseStepModel.step_action;
        String[] actionsArray = stepAction.split(";");
        for(String action:actionsArray){
            String temp = action.trim();
            if (temp.length() > 0) {
                TestCaseNode caseNode = new TestCaseNode(testCase);
                String actionName = temp.substring(0, temp.indexOf("("));
                String args = temp.substring(temp.indexOf("(") + 1, temp.indexOf(")"));
                caseNode.actionStr = actionName;
                caseNode.strValue = temp;
                caseNode.action = AndroidActionCommandType.getActionFromStr(actionName);
                caseNode.arg = args.split(",")[0];
                caseNode.args = args.split(",");
                this.actions.add(caseNode);
            }
        }
        if(caseStepModel.step_asset!=null&&caseStepModel.step_asset.trim().length()>0){
            this.assetModel = new AssetModel(testCase,caseStepModel.step_asset);
        }
        if(caseStepModel.step_error!=null&&caseStepModel.step_error.trim().length()>0){
            this.errorModel = new ErrorModel(testCase,caseStepModel.step_error);
        }

    }

    public AndroidActionCommand runNextNode(AndroidActionCommand cmd) {
        if (currentAction == -1) {
            LogUtil.debug(testCase, "[" + testCase.name + "]" + "第" + (excuteTime + 1) + "次执行" + "[" + name + "]");
        }
        AndroidActionCommand nextNode = new AndroidActionCommand();
        nextNode.SeqNo = cmd.SeqNo + 1;
        currentAction = currentAction + 1;

        if (currentAction <= actions.size()) {
            if (cmd.result == 1) {
                // 失败
                if (name.equals("waitProcess")) {
                    LogUtil.debug(testCase, "[" + testCase.name + "]" + "[" + name + "]" + "回置currentAction 1");
                    currentAction = -1;
                    return null;
                }
                if (errorModel == null) {
                    errorModel = new ErrorModel(testCase, "goto:error");
                }
                LogUtil.debug(testCase, "[" + testCase.name + "]" + "第" + excuteTime + "执行" + "[" + name + "]" + "步骤中第" + (currentAction) + "个操作失败:" + cmd.body);
                errorModel.goError();
                excuteTime = excuteTime + 1;
                LogUtil.debug(testCase, "[" + testCase.name + "]" + "[" + name + "]" + "回置currentAction 2");
                currentAction = -1;
                return null;
            } else {

                TestCaseNode lastNode = new TestCaseNode(testCase);
                if (currentAction == 0) {
                    lastNode = new TestCaseNode(testCase);
                } else if (currentAction <= actions.size()) {
                    lastNode = actions.get(currentAction - 1);
                }
                TestCaseNode node;
                if (currentAction == actions.size()) {
                    if (assetModel != null) {
                        node = assetModel.getNext();
                        if (node == null) {
                            excuteTime = excuteTime + 1;
                            assetModel.goToFail();
                            LogUtil.debug(testCase, "[" + testCase.name + "]" + "[" + name + "]" + "回置currentAction 3");
                            currentAction = -1;
                            return null;
                        }
                    } else {
                        excuteTime = excuteTime + 1;
                        if (name.equals("waitProcess")) {
                            LogUtil.debug(testCase, "[" + testCase.name + "]" + "[" + name + "]" + "回置currentAction 4");
                            currentAction = -1;
                            return null;
                        }
                        LogUtil.debug(testCase, "[" + testCase.name + "]" + "第" + excuteTime + "执行" + "[" + name + "]" + "无验证条件");
                        LogUtil.debug(testCase, "[" + testCase.name + "][" + name + "]" + "回置currentAction 5");
                        currentAction = -1;
                        return null;
                    }
                } else {
                    node = actions.get(currentAction);
                }
                LogUtil.debug(testCase, "[" + testCase.name + "][" + name + "]进入" + "[" + node.strValue + "]");
                node.creatActionCommand(nextNode, cmd, lastNode);
                if (node.action == AndroidActionCommandType.WAIT) {
                    if (node.arg.isEmpty()) {
                        node.arg = "5";
                    }
                    int timeout = 0;
                    try {
                        timeout = Integer.valueOf(node.arg.trim());
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    if (timeout == 0) {
                        timeout = 5;
                    }
                    try {
                        Thread.sleep(timeout * 1000);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        } else {


            TestCaseNode node;
            if (cmd.result == 0) {
                node = assetModel.asset(cmd.body);
            } else {
                if (name.equals("waitProcess")) {
                    LogUtil.debug(testCase, "[" + testCase.name + "]" + "[" + name + "]" + "回置currentAction 6");
                    currentAction = -1;
                    return null;
                }
                node = assetModel.assetEmpty();
            }
            if (node == null) {
                if (testCase.caseStepArray.size() > 1) {
                    //进入wait流程
                    assetModel.hasVisitWaitForProcess = true;
                    LogUtil.debug(testCase, "[" + testCase.name + "][" + name + "]进入waitProcess");
                } else {
                    excuteTime = excuteTime + 1;
                    LogUtil.debug(testCase, "[" + testCase.name + "]" + "[" + name + "]" + "回置currentAction 7");
                    assetModel.hasVisitWaitForProcess = false;
                    currentAction = -1;
                }
                return null;
            } else {
                LogUtil.debug(testCase, "[" + testCase.name + "][" + name +"]进入" + "[" + node.strValue + "]");
                node.creatActionCommand(nextNode, cmd, new TestCaseNode(testCase));
                if (node.action == AndroidActionCommandType.WAIT) {
                    if (node.arg.isEmpty()) {
                        node.arg = "5";
                    }
                    int timeout = 0;
                    try {
                        timeout = Integer.valueOf(node.arg.trim());
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    if (timeout == 0) {
                        timeout = 5;
                    }
                    try {
                        Thread.sleep(timeout * 1000);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
            return nextNode;
        }
        return nextNode;
    }

    public TestCaseStep cloneStep(TestCase cloneCase) {
        TestCaseStep cloneStep = new TestCaseStep(testCase,caseStepModel);
        cloneStep.name = name;
        if (assetModel != null) {
            cloneStep.assetModel = assetModel.cloneAsset(cloneCase);
        } else {
            cloneStep.assetModel = null;
        }
        if (errorModel != null) {
            cloneStep.errorModel = errorModel.cloneError(cloneCase);
        } else {
            cloneStep.errorModel = null;
        }
        cloneStep.testCase = cloneCase;
        cloneStep.excuteTime = excuteTime;
        cloneStep.limitTime = limitTime;
        cloneStep.currentAction = currentAction;
        cloneStep.actions = new ArrayList<TestCaseNode>();
        for (TestCaseNode caseNode : actions) {
            cloneStep.actions.add(caseNode.cloneNode(cloneCase));
        }
        return cloneStep;
    }
}
