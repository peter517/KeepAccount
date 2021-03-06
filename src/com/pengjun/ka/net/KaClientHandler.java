package com.pengjun.ka.net;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.pengjun.android.utils.AdResourceUtils;
import com.pengjun.ka.net.exception.ErrorCode;
import com.pengjun.ka.net.protobuf.KaProtocol.ArProtocol;
import com.pengjun.ka.net.protobuf.KaProtocol.ArTypeProtocol;
import com.pengjun.ka.net.protobuf.KaProtocol.KaMsg;
import com.pengjun.ka.net.protobuf.KaProtocol.MsgType;
import com.pengjun.ka.net.protobuf.KaProtocol.UserInfo;
import com.pengjun.ka.utils.KaConstants;

public class KaClientHandler extends SimpleChannelUpstreamHandler {

	private volatile Channel channel;
	private final BlockingQueue<KaMsg> kaMsgBq = new LinkedBlockingQueue<KaMsg>();

	public KaMsg sendMsg(KaMsg kaMsg) {

		KaMsg.Builder builder = KaMsg.newBuilder();

		ArProtocol.Builder arBuilder = ArProtocol.newBuilder();

		arBuilder.setAccount(2);
		arBuilder.setCreateDate(AdResourceUtils.getLocalIpAddress());
		arBuilder.setId(1);
		arBuilder.setTypeId(1);
		arBuilder.setUpdateTime("");

		ArTypeProtocol.Builder arTypeBuilder = ArTypeProtocol.newBuilder();
		arTypeBuilder.setCreateDate(AdResourceUtils.getLocalIpAddress());
		arTypeBuilder.setId(1);
		arTypeBuilder.setTypeName("test");
		arTypeBuilder.setUpdateTime("");
		arTypeBuilder.setImgResName("");

		builder.addArProtocol(arBuilder);
		builder.addArTypeProtocol(arTypeBuilder);
		builder.setMsgType(MsgType.BACKUP);

		UserInfo.Builder userInfoBuilder = UserInfo.newBuilder();
		userInfoBuilder.setUserName("pj");
		userInfoBuilder.setPassword("123");
		builder.setUserInfo(userInfoBuilder);

		channel.write(builder.build());

		KaMsg revKaMsg;
		boolean interrupted = false;
		for (;;) {
			try {
				revKaMsg = kaMsgBq.poll(3000, TimeUnit.MILLISECONDS);
				break;
			} catch (InterruptedException e) {
				interrupted = true;
			}
		}

		if (revKaMsg == null) {
			KaConstants.clientLogger.error("ErrorCode = " + ErrorCode.NetError);
			return null;
		}
		if (interrupted) {
			Thread.currentThread().interrupt();
		}

		return revKaMsg;

	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		KaConstants.clientLogger.debug("connect sucessed!");
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		if (e instanceof ChannelStateEvent) {
			KaConstants.clientLogger.info(e.toString());
		}
		super.handleUpstream(ctx, e);
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		channel = e.getChannel();
		super.channelOpen(ctx, e);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) {
		boolean offered = kaMsgBq.offer((KaMsg) e.getMessage());
		assert offered;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		KaConstants.clientLogger.error("Unexpected exception from downstream.", e.getCause());
		e.getChannel().close();
	}
}
