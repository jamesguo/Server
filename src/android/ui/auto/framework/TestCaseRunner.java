package android.ui.auto.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import android.ui.auto.framework.command.AndroidActionCommand;
import android.ui.auto.framework.command.AndroidActionCommandType;
import android.ui.auto.framework.log.LogUtil;
import android.ui.auto.framework.util.CommondProcoltolUtil;
import android.ui.auto.framework.util.TypeConvertUtil;

public class TestCaseRunner {
	public Socket client;
	public final int READ_BUFFER_LENGTH = 1024;
	public InputStream inputStream;
	public PrintWriter out;
	public TestCase testCase;
	public boolean finish = false;
	public TestCaseRunner(TestCase testCase, Socket client) {
		this.testCase = testCase;
		this.client = client;
	}

	public void start() {
		testCase.reset(true);
		if (testCase != null && testCase.testSteps.size() > 0) {
			testCase.startCase();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				listener();
				LogUtil.error(testCase, "=======================分割线=========================");
				// testCase.reset(false);
				try {
					client.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "TestCaseRunnerListener" + LogUtil.getFormatTime() + testCase.name).start();
		LogUtil.error(testCase, "开始执行Case Step 队列中剩余" + testCase.caseStepArray.size() + "个操作，下一个操作为" + (testCase.caseStepArray.size() > 0 ? ("" + testCase.caseStepArray.peek().name) : ""));
		runCommand(new AndroidActionCommand());

	}

	public boolean isConnected() {
		if (client != null && !client.isClosed() && client.isConnected()) {
			return true;
		}
		return false;
	}

	private void listener() {
		while (!finish) {
			if (isConnected()) {
				try {
					inputStream = client.getInputStream();
					out = new PrintWriter(client.getOutputStream(), true);
					byte[] buffer = new byte[4];
					int lenght = 0;
					try {
						lenght = inputStream.read(buffer);
					} catch (Exception e) {
						e.printStackTrace();
						break;
					}
					int maxLength = -1;
					if (lenght == 4) {
						try {
							maxLength = TypeConvertUtil.bytesToInt(buffer);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						try {
							throw new RuntimeException("readByteSize=" + lenght + ",it must be 4!");
						} catch (RuntimeException e) {
							e.printStackTrace();
						}
						break;
					}
					if (maxLength != -1) {
						byte[] inputeByte = null;
						try {
							inputStream = client.getInputStream();
							inputeByte = new byte[maxLength];
							if (maxLength > READ_BUFFER_LENGTH) {
								int readLength = 0;
								int readIndex = 0;
								while (readIndex < maxLength) {
									if (maxLength - readIndex > READ_BUFFER_LENGTH) {
										readLength = inputStream.read(inputeByte, readIndex, READ_BUFFER_LENGTH);
									} else {
										readLength = inputStream.read(inputeByte, readIndex, maxLength - readIndex);
									}
									if (readLength == -1) {
										while (readIndex < maxLength) {
											inputeByte[readIndex] = 32;
											readIndex++;
										}
										break;
									} else {
										readIndex += readLength;
									}
								}
							} else {
								inputStream.read(inputeByte);
							}
							final byte[] response = inputeByte;
							new Thread(new Runnable() {

								@Override
								public void run() {
									AndroidActionCommand actionCommand = CommondProcoltolUtil.buileActionCommand(response);
									runCommand(actionCommand);
								}
							}).start();
						} catch (Exception e) {
							e.printStackTrace();
							break;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			} else {
				break;
			}
		}
	}

	public synchronized void sendRequest(final AndroidActionCommand actionCommandResult) {
		byte[] dataBean = CommondProcoltolUtil.buileResponse(actionCommandResult);
		OutputStream outputStream;
		try {
			outputStream = client.getOutputStream();
			outputStream.write(dataBean);
			outputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private synchronized void runCommand(final AndroidActionCommand cmd) {
		// cmd.excutor();
		if(cmd.actionCode==AndroidActionCommandType.FINISH){
			finish = true;
			return;
		}
		AndroidActionCommand actionCommandResult = testCase.runNextNode(cmd);
		
		if (actionCommandResult != null) {
			if (actionCommandResult.actionCode != AndroidActionCommandType.WAIT) {
				sendRequest(actionCommandResult);
			} else {
				runCommand(actionCommandResult);
			}
		}
	}
}
