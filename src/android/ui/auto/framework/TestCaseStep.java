package android.ui.auto.framework;

import android.ui.auto.framework.command.AndroidActionCommand;
import android.ui.auto.framework.command.AndroidActionCommandType;
import android.ui.auto.framework.log.LogUtil;

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

    public TestCaseStep(TestCase testCase) {
        this.testCase = testCase;
    }

    public AndroidActionCommand runNextNode(AndroidActionCommand cmd) {
        if (currentAction == -1) {
            LogUtil.debug(testCase, "[" + testCase.name + "]" + "��" + (excuteTime + 1) + "��ִ��" + "[" + name + "]");
        }
        AndroidActionCommand nextNode = new AndroidActionCommand();
        nextNode.SeqNo = cmd.SeqNo + 1;
        currentAction = currentAction + 1;

        if (currentAction <= actions.size()) {
            // ��ִ�еĽ��
            if (cmd.result == 1) {
                // ʧ��
                if (name.equals("waitProcess")) {
                    currentAction = -1;
                    return null;
                }
                if (errorModel == null) {
                    errorModel = new ErrorModel(testCase, "goto:error");
                }
                errorModel.goError();
                excuteTime = excuteTime + 1;
                LogUtil.debug(testCase, "[" + testCase.name + "]" + "��" + excuteTime + "��ִ��" + "[" + name + "]" + "�ĵ�" + (currentAction) + "����ʧ��:" + cmd.body);
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
                            // LogUtil.debug(testCase, "[" + testCase.name + "]"
                            // + "��" + excuteTime + "��ִ��" + "[" + name + "]��" +
                            // (assetModel.currentOffset) + "������֤ʧ��");
                            assetModel.goToFail();
                            currentAction = -1;
                            return null;
                        }
                    } else {
                        excuteTime = excuteTime + 1;
                        if (name.equals("waitProcess")) {
                            currentAction = -1;
                            return null;
                        }
                        LogUtil.debug(testCase, "[" + testCase.name + "]" + "��" + excuteTime + "��ִ��" + "[" + name + "]" + "ȱ�� ��ָ֤��");
                        currentAction = -1;
                        return null;
                    }
                } else {
                    node = actions.get(currentAction);
                }
                LogUtil.debug(testCase, "[" + testCase.name + "]ִ��" + "[" + node.strValue + "]");
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
                // ʧ��
                if (name.equals("waitProcess")) {
                    currentAction = -1;
                    return null;
                }
                node = assetModel.assetEmpty();
            }
            if (node == null) {
                if (testCase.caseStepArray.size() > 1) {
                    // ����waitProcess
                    LogUtil.debug(testCase, "[" + testCase.name + "]" + "���� waitProcess");
                } else {
                    // ��������step
                    excuteTime = excuteTime + 1;
                    currentAction = -1;
                }
                return null;
            } else {
                LogUtil.debug(testCase, "[" + testCase.name + "]ִ��" + "[" + node.strValue + "]");
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
//			
//			
//			if (cmd.result == 0) {
//				String target = assetModel.getCurrent().args[1].trim().replace("goto:", "");
//				if (target.equals("waitProcess")) {
//					assetModel.getCurrent().assetSuccess();
//					LogUtil.debug(testCase, "[" + testCase.name + "]" + "��" + (excuteTime + 1) + "��ִ��" + "[" + name + "] waitProcess ��֤�ɹ�");
//					LogUtil.error(testCase, "Step ������ʣ��" + testCase.caseStepArray.size() + "����������һ������Ϊ" + (testCase.caseStepArray.size() > 0 ? ("" + testCase.caseStepArray.peek().name) : ""));
//				} else {
//					assetModel.getCurrent().assetSuccess();
//					excuteTime = excuteTime + 1;
//					LogUtil.debug(testCase, "[" + testCase.name + "]" + "��" + excuteTime + "��ִ��" + "[" + name + "]��" + (assetModel.currentOffset + 1) + "��֤�ɹ�");
//					currentAction = -1;
//					assetModel.currentOffset = -1;
//				}
//				return null;
//			} else {
//				TestCaseNode node;
//				node = assetModel.getNext();
//				if (node == null) {
//					excuteTime = excuteTime + 1;
//					LogUtil.debug(testCase, "[" + testCase.name + "]" + "��" + excuteTime + "��ִ��" + "[" + name + "]��" + (assetModel.currentOffset) + "������֤ʧ��");
//					assetModel.goToFail();
//					currentAction = -1;
//					return null;
//				}
//				if (node.action == AndroidActionCommandType.SEE && node.arg.isEmpty()) {
//					String target = node.args[1].trim().replace("goto:", "");
//					if (target.equals("continue")) {
//						return null;
//					} else {
//						assetModel.goToSuccess();
//						currentAction = -1;
//						return null;
//					}
//				}
//
//				node.creatActionCommand(nextNode, cmd, new TestCaseNode(testCase));
//				if (node.action == AndroidActionCommandType.WAIT) {
//					if (node.arg.isEmpty()) {
//						node.arg = "5";
//					}
//					int timeout = 0;
//					try {
//						timeout = Integer.valueOf(node.arg.trim());
//					} catch (Exception e) {
//						// TODO: handle exception
//					}
//					if (timeout == 0) {
//						timeout = 5;
//					}
//					try {
//						Thread.sleep(timeout * 1000);
//					} catch (Exception e) {
//						// TODO: handle exception
//					}
//				}
//			}
        }
        return nextNode;
    }

    public TestCaseStep cloneStep(TestCase cloneCase) {
        // TODO Auto-generated method stub
        TestCaseStep cloneStep = new TestCaseStep(cloneCase);
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
