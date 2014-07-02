package auto.server.core;


import auto.server.bean.TestCase;
import auto.server.log.LogUtil;
import auto.server.util.TypeConvertUtil;

import java.net.Socket;

public class TestCaseThread extends Thread {
    TestCase testCase;
    Socket oneClient;

    public TestCaseThread(TestCase testCase, Socket oneClient) {
        this.testCase = testCase;
        this.oneClient = oneClient;
        setName("TestRunner" + TypeConvertUtil.getFormatTime() + testCase.name);
    }

    @Override
    public synchronized void start() {
        // TODO Auto-generated method stub
        new TestCaseRunner(testCase, oneClient).start();
    }

}
