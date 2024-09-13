package com.chen.nettyDemo.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
@Slf4j
public class TcpServer implements ITcpServer {

    private String ip;

    private int port;

    private boolean isUseEpoll;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    public Channel channel;

    @Override
    public void start(String ip, int port, boolean isUseEpoll) {
        log.info("初始化 TCP server :" + ip + ":" + port);
        this.ip = ip;
        this.port = port;
        this.isUseEpoll = isUseEpoll;
        bossGroup = isUseEpoll ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        workerGroup = isUseEpoll ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        this.tcpServer();
    }


    /**
     * 初始化
     */
    private void tcpServer() {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap().group(bossGroup, workerGroup).channel(isUseEpoll ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    // tcp缓冲区
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 将网络数据积累到一定的数量后,服务器端才发送出去,会造成一定的延迟。希望服务是低延迟的,建议将TCP_NODELAY设置为true
                    // .childOption(ChannelOption.TCP_NODELAY, false)
                    // 保持长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    // 配置 编码器、解码器、业务处理
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new TcpServerHandler());
                        }
                    });
            // 绑定端口，开始接收进来的连接
            ChannelFuture future = serverBootstrap.bind(ip, port).sync();
            channel = future.channel();
            log.info("Tcp Server启动成功！开始监听端口：{}", ip + ":" + port);
            future.channel().closeFuture().sync();
            log.info("Tcp Server连接已关闭===");
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Tcp Server异常--------------");
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 销毁
     */
    @PreDestroy
    @Override
    public void destroy() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

}
