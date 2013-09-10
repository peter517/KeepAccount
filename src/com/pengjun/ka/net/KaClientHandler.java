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

import com.pengjun.ka.net.exception.ErrorCode;
import com.pengjun.ka.net.protobuf.KaProtocol.KaMsg;
import com.pengjun.ka.net.protobuf.KaProtocol.MsgType;
import com.pengjun.ka.utils.AndroidLoggerUtils;
import com.pengjun.ka.utils.MyDebug;

public class KaClientHandler extends SimpleChannelUpstreamHandler {

	private volatile Channel channel;
	private final BlockingQueue<KaMsg> kaMsgBq = new LinkedBlockingQueue<KaMsg>();

	public KaMsg sendMsg(KaMsg kaMsg) {

		KaMsg.Builder builder = KaMsg.newBuilder();

		// ArProtocol.Builder arBuilder = ArProtocol.newBuilder();
		//
		// arBuilder.setAccount(2);
		// arBuilder.setCreateDate(ResourceUtils.getLocalIpAddress());
		// arBuilder.setId(1);
		// arBuilder.setTypeId(1);
		// arBuilder.setUpdateTime("");
		//
		// ArTypeProtocol.Builder arTypeBuilder = ArTypeProtocol.newBuilder();
		// arTypeBuilder.setCreateDate(ResourceUtils.getLocalIpAddress());
		// arTypeBuilder.setId(1);
		// arTypeBuilder.setTypeName("test");
		// arTypeBuilder.setUpdateTime("");
		// arTypeBuilder.setImgResName("");

		builder.setMsgType(MsgType.LOGIN);
		// builder.addArProtocol(arBuilder);
		// builder.addArTypeProtocol(arTypeBuilder);

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
			AndroidLoggerUtils.clientLogger.error("ErrorCode = " + ErrorCode.NetError);
			return null;
		}
		MyDebug.printFromPJ("revArType.getImgResName()" + revKaMsg.getMsgType());
		if (interrupted) {
			Thread.currentThread().interrupt();
		}

		return revKaMsg;

	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		AndroidLoggerUtils.clientLogger.debug("connect sucessed!");
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		if (e instanceof ChannelStateEvent) {
			AndroidLoggerUtils.clientLogger.info(e.toString());
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
		AndroidLoggerUtils.clientLogger.error("Unexpected exception from downstream.", e.getCause());
		e.getChannel().close();
	}
}
