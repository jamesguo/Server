package auto.server.core;

import auto.server.bean.TestCase;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class AutoServerSocket {
    ServerSocket serverSocket;

    public AutoServerSocket(final int port) throws SocketException {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(0);
        } catch (Exception e) {
            throw new SocketException("start socket server failed on " + port);
        }
    }

    public void startListener() throws SocketException {
        while (!AutoServer.allCaseFinished) {
            try {
                Socket client = serverSocket.accept();
                //60s超时，防止程序卡死
                client.setSoTimeout(0);
//                client.setSoTimeout(60 * 1000);
                final TestCase testCase = AutoServer.getReadyTestCase();
                final Socket oneClient = client;
                if (testCase != null) {
                    new TestCaseThread(testCase, oneClient).start();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                break;
            }
        }

    }

}
