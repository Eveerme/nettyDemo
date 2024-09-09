package com.chen.nettyDemo.netty.server;

import com.chen.nettyDemo.netty.thread.QueueParams;
import com.chen.nettyDemo.netty.thread.ThreadUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private byte[] content;

    /*
     * 覆盖channelActive 方法在channel被启用的时候触发（在建立连接的时候）
     * 覆盖了 channelActive() 事件处理方法。服务端监听到客户端活动
     */
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("UDP 服务端连接成功！！");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
        ByteBuf msg = packet.content();
        // 获取源地址
        InetSocketAddress sender = packet.sender();
        String remoteAddress = sender.getAddress().getHostAddress() + ":" + sender.getPort();

        // 获取目的地址
        InetSocketAddress recipient = packet.recipient();
        String localAddress = recipient.getAddress().getHostAddress() + ":" + recipient.getPort();

        log.info("UDP 服务端：{}>>>{}", remoteAddress, localAddress);
        content = new byte[msg.readableBytes()];
        msg.readBytes(content);
        log.info("UDP 服务端收到消息{}", content.length);
        QueueParams queueParams = new QueueParams();
        queueParams.setKey(remoteAddress + localAddress);
        queueParams.setData(content);
        ThreadUtils.udpServerSend.putQueue(queueParams);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("UDP 服务端发生异常," + cause.getMessage());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.error("UDP 服务端连接断开");
    }
}
