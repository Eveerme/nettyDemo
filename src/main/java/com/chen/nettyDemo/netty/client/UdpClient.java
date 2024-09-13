package com.chen.nettyDemo.netty.client;

import com.chen.nettyDemo.netty.NettyUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UdpClient {

    private EventLoopGroup WORKER_GROUP;

    private Bootstrap bootstrap;

    public Channel channel;

    public String ip;
    public int port;
    public boolean isUseEpoll;

    public UdpClient() {
        ip = NettyUtils.udpClientip;
        port = NettyUtils.udpClientport;
        isUseEpoll = NettyUtils.udpClientuseEpoll;
    }

    public void connectAndSendMsg(String key, byte[] msg) {
        log.info("初始化Udp Client :{}:{}", ip, port);
        WORKER_GROUP = isUseEpoll ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        try {
            bootstrap = new Bootstrap()
                    .channel(isUseEpoll ? EpollDatagramChannel.class : NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<DatagramChannel>() {
                        @Override
                        protected void initChannel(DatagramChannel ch) throws Exception {
                            ch.pipeline().addLast(new UdpClientHandler(key));
                        }
                    });
            bootstrap.group(WORKER_GROUP);
            ChannelFuture future = bootstrap.connect(ip, port).sync();
            if (future.isSuccess()) {
                log.info("Udp Client 连接服务端成功！！");
                channel = future.channel();
                NettyUtils.udpClientChannelMap.put(key, channel);
                sendMsg(msg);
                future.channel().closeFuture().sync();
                log.info("Udp Client 连接已关闭===");
            }
        } catch (Exception e) {
            System.out.println("Udp Client 异常。。。");
        } finally {
            WORKER_GROUP.shutdownGracefully();
        }
    }

    public void sendMsg(byte[] msg) {
        log.info("Udp Client channel 发送");
        channel.writeAndFlush(Unpooled.copiedBuffer(msg));
    }

}
