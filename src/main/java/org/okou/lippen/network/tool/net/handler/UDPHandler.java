package org.okou.lippen.network.tool.net.handler;

import java.io.IOException;

import javax.swing.JOptionPane;

import org.okou.lippen.network.tool.listener.MessageReceivedListener;
import org.okou.lippen.network.tool.model.DataManager;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

@Sharable
public class UDPHandler extends SimpleChannelInboundHandler<DatagramPacket> {
	private DataManager data;
	private MessageReceivedListener listener;
	public UDPHandler(DataManager data, MessageReceivedListener listener){
		this.data = data;
		this.listener = listener;
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
		data.addConnect(ctx.channel());
		ByteBuf buf = msg.copy().content();
		byte[] data = new byte[buf.readableBytes()];
		buf.readBytes(data);
		listener.messageReceived(data);
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if(cause instanceof IOException) {
			data.removeConnect(ctx.channel());
			return;
		}
		JOptionPane.showMessageDialog(data.getComponent(), "�������쳣", "�������쳣", JOptionPane.OK_OPTION);
	}
}