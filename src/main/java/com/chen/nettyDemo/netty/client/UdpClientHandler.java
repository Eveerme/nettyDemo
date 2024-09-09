package com.chen.nettyDemo.netty.client;

import com.chen.nettyDemo.netty.thread.QueueParams;
import com.chen.nettyDemo.netty.thread.ThreadUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

@Slf4j
public class UdpClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private byte[] content;

    private String key;

    public UdpClientHandler(String key) {
        this.key = key;
    }

    /*
     * 覆盖channelActive 方法在channel被启用的时候触发（在建立连接的时候）
     * 覆盖了 channelActive() 事件处理方法。服务端监听到客户端活动
     */
    public void channelActive(ChannelHandlerContext ctx) {
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        SocketAddress localAddress = ctx.channel().localAddress();
        log.info("Netty UDP客户端：{}>>>{}", localAddress, remoteAddress + "连接成功！！");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        ByteBuf msg = packet.content();
        // 发送方
        InetSocketAddress sender = packet.sender();
        String remoteAddress = sender.getAddress().getHostAddress() + ":" + sender.getPort();

        // 接收方
        InetSocketAddress recipient = packet.recipient();
        String localAddress = recipient.getAddress().getHostAddress() + ":" + recipient.getPort();
        log.info("UDP 客户端：{}>>>{}", remoteAddress, localAddress);
        content = new byte[msg.readableBytes()];
        msg.readBytes(content);
        log.info("UDP 客户端收到消息{}", content.length);
        QueueParams queueParams = new QueueParams();
        queueParams.setKey(key);
        queueParams.setData(content);
        ThreadUtils.udpClientSend.putQueue(queueParams);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("UDP 客户端发生异常," + cause.getMessage());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.error("UDP 客户端断开连接");
    }
}
