package android.ui.auto.framework;

import android.ui.auto.framework.log.LogUtil;

import java.net.Socket;

public class TestCaseThread extends Thread {
    TestCase testCase;
    Socket oneClient;

    public TestCaseThread(TestCase testCase, Socket oneClient) {
        this.testCase = testCase;
        this.oneClient = oneClient;
        setName("TestRunner" + LogUtil.getFormatTime() + testCase.name);
    }

    @Override
    public synchronized void start() {
        // TODO Auto-generated method stub
        new TestCaseRunner(testCase, oneClient).start();
    }

}
