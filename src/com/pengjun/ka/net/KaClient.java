package com.pengjun.ka.net;

import com.pengjun.net.BaseNettyClient;

public class KaClient extends BaseNettyClient {

	private KaClient(String serverIp, int port) {
		super(serverIp, port);
	}

	private final static String REMOTE_IP = "10.69.17.167";
	// private final static String REMOTE_IP = "192.168.1.101";
	private final static int REMOTE_PORT = 8000;
	private KaClientHandler handler = null;

	private static KaClient kaClient;

	public static KaClient getInstance() {
		if (kaClient == null) {
			kaClient = new KaClient(REMOTE_IP, REMOTE_PORT);
		}
		return kaClient;
	}

	public void connect() {
		handler = (KaClientHandler) super.connect(new KaClientPipelineFactory(), KaClientHandler.class);
	}

	public void sendData() {
		if (handler != null) {
			handler.sendMsg(null);
		}
	}

	public void disConnect() {
		super.disConnect();
	}

}
