package com.pengjun.ka.net;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.pengjun.ka.utils.AndroidLoggerUtils;

public class KaClient {

	private final String REMOTE_IP = "10.9.9.13";
	private final int REMOTE_PORT = 8000;
	private Channel channel = null;
	private ClientBootstrap bootstrap = null;
	private KaClientHandler handler = null;

	private static KaClient kaClient;

	public static KaClient getInstance() {
		if (kaClient == null) {
			kaClient = new KaClient();
		}
		return kaClient;
	}

	public void connect() {

		bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new KaClientPipelineFactory());

		AndroidLoggerUtils.clientLogger.info("client connect: " + "remote_ip " + REMOTE_IP + " remote report "
				+ REMOTE_PORT);

		ChannelFuture connectFuture = bootstrap.connect(new InetSocketAddress(REMOTE_IP, REMOTE_PORT));
		channel = connectFuture.awaitUninterruptibly().getChannel();
		handler = channel.getPipeline().get(KaClientHandler.class);

	}

	public void sendData() {
		if (handler != null) {
			handler.sendMsg(null);
		}
	}

	public void disConnect() {
		if (channel != null) {
			channel.close().awaitUninterruptibly();
		}
		if (bootstrap != null) {
			bootstrap.releaseExternalResources();
		}
	}

}
