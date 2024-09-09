package com.chen.nettyDemo.netty.server;

import com.chen.nettyDemo.netty.thread.QueueParams;
import com.chen.nettyDemo.netty.NettyUtils;
import com.chen.nettyDemo.netty.thread.ThreadUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;

@Slf4j
public class TcpServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private byte[] content;

    /*
     * 覆盖channelActive 方法在channel被启用的时候触发（在建立连接的时候）
     * 覆盖了 channelActive() 事件处理方法。服务端监听到客户端活动
     */
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.channel().config().setAutoRead(false);
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        SocketAddress localAddress = ctx.channel().localAddress();
        log.info("TCP 服务端：{}>>>{}", remoteAddress, localAddress + "连接成功！！");
        NettyUtils.tcpServerChannelMap.put(remoteAddress.toString() + localAddress.toString(), ctx.channel());
        new Thread(() -> NettyUtils.startClientConnect(NettyUtils.tcpClientip, NettyUtils.tcpClientport, NettyUtils.tcpClientuseEpoll, remoteAddress.toString() + localAddress.toString())).start();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        SocketAddress localAddress = ctx.channel().localAddress();
        log.info("TCP 服务端：{}>>>{}", localAddress, remoteAddress);
        content = new byte[msg.readableBytes()];
        msg.readBytes(content);
        log.info("TCP 服务端收到消息" + content.length);
        QueueParams queueParams = new QueueParams();
        queueParams.setKey(remoteAddress.toString() + localAddress.toString());
        queueParams.setData(content);
        ThreadUtils.tcpServerSend.putQueue(queueParams);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("TCP 服务端发生异常," + cause.getMessage());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.error("TCP 服务端连接断开");
    }
}
