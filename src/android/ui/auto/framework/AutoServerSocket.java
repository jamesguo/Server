package android.ui.auto.framework;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class AutoServerSocket {
	ServerSocket serverSocket;
	public AutoServerSocket(final int port) throws SocketException {
		try {
			serverSocket = new ServerSocket(port);
		} catch (Exception e) {
			throw new SocketException("start socket server failed on " + port);
		}
	}

	public void startListener() throws SocketException {
		while (!AutoServer.allCaseFinished) {
			try {
				final Socket client = serverSocket.accept();
				client.setSoTimeout(0);
				final TestCase testCase = AutoServer.getReadyTestCase();
				if (testCase != null) {

					new Thread(new Runnable() {

						@Override
						public void run() {
							new TestCaseRunner(testCase, client).start();
						}
					}, "TestRunner" + testCase.name).start();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}

	}

}
