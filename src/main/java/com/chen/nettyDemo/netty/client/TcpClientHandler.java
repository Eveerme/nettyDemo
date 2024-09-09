package com.chen.nettyDemo.netty.client;

import com.chen.nettyDemo.netty.NettyUtils;
import com.chen.nettyDemo.netty.thread.QueueParams;
import com.chen.nettyDemo.netty.thread.ThreadUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;

@Slf4j
public class TcpClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private byte[] content;

    private String key;

    public TcpClientHandler(String key) {
        this.key = key;
    }

    /*
     * 覆盖channelActive 方法在channel被启用的时候触发（在建立连接的时候）
     * 覆盖了 channelActive() 事件处理方法。服务端监听到客户端活动
     */
    public void channelActive(ChannelHandlerContext ctx) {
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        SocketAddress localAddress = ctx.channel().localAddress();
        log.info("Netty TCP客户端：{}>>>{}", localAddress, remoteAddress + "连接成功！！");
        NettyUtils.tcpClientChannelMap.put(key, ctx.channel());
        NettyUtils.setServerChannelAutoRead(key);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        SocketAddress localAddress = ctx.channel().localAddress();
        log.info("TCP 客户端：{}>>>{}", localAddress, remoteAddress);
        content = new byte[msg.readableBytes()];
        msg.readBytes(content);
        log.info("TCP 客户端收到消息{}", content.length);
        QueueParams queueParams = new QueueParams();
        queueParams.setKey(key);
        queueParams.setData(content);
        ThreadUtils.tcpClientSend.putQueue(queueParams);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("TCP 客户端发生异常," + cause.getMessage());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.error("TCP 客户端断开连接");
    }
}
