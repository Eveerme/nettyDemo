package com.chen.nettyDemo.netty.server;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
@Slf4j
public class UdpServer implements IUdpServer {

    private String ip;

    private int port;

    private boolean isUseEpoll;

    private EventLoopGroup bossGroup;


    public Channel channel;

    @Override
    public void start(String ip, int port, boolean isUseEpoll) {
        log.info("初始化 UDP server :{}:{}", ip, port);
        this.ip = ip;
        this.port = port;
        this.isUseEpoll = isUseEpoll;
        bossGroup = isUseEpoll ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        this.udpServer();
    }


    /**
     * 初始化
     */
    private void udpServer() {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(bossGroup).channel(isUseEpoll ? EpollDatagramChannel.class : NioDatagramChannel.class)
                    // 配置 编码器、解码器、业务处理
                    .handler(new ChannelInitializer<DatagramChannel>() {
                        @Override
                        protected void initChannel(DatagramChannel ch) throws Exception {
                            ch.pipeline().addLast(new UdpServerHandler());
                        }
                    });
            // 绑定端口，开始接收进来的连接
            ChannelFuture future = bootstrap.bind(ip, port).sync();
            channel = future.channel();
            log.info("Udp Server启动成功！开始监听端口：{}", ip + ":" + port);
            future.channel().closeFuture().sync();
            log.info("Udp Server连接已关闭===");
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Udp Server异常--------------");
        } finally {
            bossGroup.shutdownGracefully();
        }
    }

    /**
     * 销毁
     */
    @PreDestroy
    @Override
    public void destroy() {
        bossGroup.shutdownGracefully();
    }

}
