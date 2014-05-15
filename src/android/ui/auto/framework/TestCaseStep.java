package android.ui.auto.framework;

import java.util.ArrayList;

import android.ui.auto.framework.command.AndroidActionCommand;
import android.ui.auto.framework.command.AndroidActionCommandType;
import android.ui.auto.framework.log.LogUtil;

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
			// ����ִ�еĽ��
			if (cmd.result == 1) {
				// ʧ��
				if (errorModel == null) {
					errorModel = new ErrorModel(testCase, "goto:error");
				}
				errorModel.goError();
				excuteTime = excuteTime + 1;
				LogUtil.debug(testCase, "[" + testCase.name + "]" + "��" + excuteTime + "��ִ��" + "[" + name + "]" + "�ĵ�" + (currentAction + 1) + "����ʧ��:" + cmd.body);
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
							LogUtil.debug(testCase, "[" + testCase.name + "]" + "��" + excuteTime + "��ִ��" + "[" + name + "]��" + (assetModel.currentOffset) + "������֤ʧ��");
							assetModel.goToFail();
							currentAction = -1;
							return null;
						}
					} else {
						excuteTime = excuteTime + 1;
						LogUtil.debug(testCase, "[" + testCase.name + "]" + "��" + excuteTime + "��ִ��" + "[" + name + "]" + "ȱ�� ��ָ֤��");
						currentAction = -1;
						return null;
					}
				} else {
					node = actions.get(currentAction);
				}
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
			if (cmd.result == 0) {
				excuteTime = excuteTime + 1;
				LogUtil.debug(testCase, "[" + testCase.name + "]" + "��" + excuteTime + "��ִ��" + "[" + name + "]��" + (assetModel.currentOffset + 1) + "��֤�ɹ�");
				assetModel.goToSuccess();
				currentAction = -1;
				return null;
			} else {
				TestCaseNode node;
				node = assetModel.getNext();
				if (node == null) {
					excuteTime = excuteTime + 1;
					LogUtil.debug(testCase, "[" + testCase.name + "]" + "��" + excuteTime + "��ִ��" + "[" + name + "]��" + (assetModel.currentOffset) + "������֤ʧ��");
					assetModel.goToFail();
					currentAction = -1;
					return null;
				}
				if (node.action == AndroidActionCommandType.SEE && node.arg.isEmpty()) {
					String target = node.args[1].trim().replace("goto:", "");
					if (target.equals("continue")) {
						return runNextNode(cmd);
					} else {
						assetModel.goToSuccess();
						return null;
					}
				}

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
		}
		return nextNode;
	}
}
