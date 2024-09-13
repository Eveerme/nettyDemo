package com.chen.nettyDemo.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
@Slf4j
public class TcpClient implements ITcpClient {

    private String ip;

    private int port;

    private boolean isUseEpoll;

    private EventLoopGroup WORKER_GROUP;

    private SocketChannel socketChannel;

    private Bootstrap bootstrap;

    public Channel channel;

    @Override
    public void start(String ip, int port, boolean isUseEpoll, String key) {
        log.info("初始化TCP Client ...{}:{}", ip, port);
        this.ip = ip;
        this.port = port;
        this.isUseEpoll = isUseEpoll;
        WORKER_GROUP = isUseEpoll ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        this.tcpClient(key);
    }

    @Override
    public void reconnect() throws Exception {
        if (socketChannel != null && socketChannel.isActive()) {
            socketChannel.close();
            this.connect();
        }

    }

    public void disconnect() {
        if (socketChannel != null && socketChannel.isActive()) {
            socketChannel.close();
        }
    }

    /**
     * Client初始化
     */
    private void tcpClient(String key) {
        try {
            bootstrap = new Bootstrap()
                    .channel(isUseEpoll ? EpollSocketChannel.class : NioSocketChannel.class)
                    // .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new TcpClientHandler(key));
                        }
                    })
                    .remoteAddress(ip, port);
            bootstrap.group(WORKER_GROUP);
            this.connect();
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println("TCP 异常。。。");
        } finally {
            WORKER_GROUP.shutdownGracefully();
        }
    }

    /**
     * 连接服务器
     */
    public void connect() throws InterruptedException {
        this.disconnect();
        ChannelFuture future = bootstrap.connect().sync();
        channel = future.channel();
        if (future.isSuccess()) {
            socketChannel = (SocketChannel) future.channel();
            log.info("TCP Client 连接服务端成功！！");
            future.channel().closeFuture().sync();
            log.info("TCP 客户端连接已关闭===");
        }
    }


    /**
     * 销毁
     */
    @PreDestroy
    @Override
    public void destroy() {
        WORKER_GROUP.shutdownGracefully();
        socketChannel.closeFuture();
    }

    /**
     * 获取频道
     */
    public SocketChannel getSocketChannel() {
        return this.socketChannel;
    }
}
