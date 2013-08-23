package com.pengjun.ka.net;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.net.exception.ErrorCode;
import com.pengjun.ka.net.protobuf.KaProtocol.ArProtocol;
import com.pengjun.ka.utils.MyDebug;
import com.pengjun.ka.utils.ResManageUtils;

public class KaClientHandler extends SimpleChannelUpstreamHandler {

	private static final Logger logger = Logger.getLogger(KaClientHandler.class.getName());
	private volatile Channel channel;
	private final BlockingQueue<ArProtocol> answer = new LinkedBlockingQueue<ArProtocol>();

	public ArProtocol sendArList(AccountRecord sendAr) {

		ArProtocol.Builder builder = ArProtocol.newBuilder();

		builder.setAccount(2);
		builder.setCreateDate(ResManageUtils.getLocalIpAddress());
		builder.setId(1);
		builder.setTypeId(1);
		builder.setUpdateTime(sendAr.getUpdateTime());

		channel.write(builder.build());

		ArProtocol revAr;
		boolean interrupted = false;
		for (;;) {
			try {
				revAr = answer.poll(3000, TimeUnit.MILLISECONDS);
				break;
			} catch (InterruptedException e) {
				interrupted = true;
			}
		}

		if (revAr == null) {
			MyDebug.printFromPJ("ErrorCode = " + ErrorCode.NetError);
			return null;
		}
		MyDebug.printFromPJ("revAr.getAccount()" + revAr.getAccount());
		if (interrupted) {
			Thread.currentThread().interrupt();
		}

		return revAr;

	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		MyDebug.printFromPJ("connect sucessed!");
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		if (e instanceof ChannelStateEvent) {
			logger.info(e.toString());
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
		boolean offered = answer.offer((ArProtocol) e.getMessage());
		assert offered;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.log(Level.WARNING, "Unexpected exception from downstream.", e.getCause());
		e.getChannel().close();
	}
}
