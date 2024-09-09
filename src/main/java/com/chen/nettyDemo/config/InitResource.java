package com.chen.nettyDemo.config;

import com.chen.nettyDemo.netty.NettyUtils;
import com.chen.nettyDemo.netty.thread.ThreadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitResource implements ApplicationRunner {
    @Value("${netty.tcp.server.ip}")
    private String tcpServerIp;

    @Value("${netty.tcp.server.port}")
    private int tcpServerPort;

    @Value("${netty.tcp.server.use-epoll}")
    private boolean tcpServerUseEpoll;

    @Value("${netty.tcp.client.ip}")
    private String tcpClientIp;

    @Value("${netty.tcp.client.port}")
    private int tcpClientPort;

    @Value("${netty.tcp.client.use-epoll}")
    private boolean tcpClientUseEpoll;

    @Value("${netty.udp.server.ip}")
    private String udpServerIp;

    @Value("${netty.udp.server.port}")
    private int udpServerPort;

    @Value("${netty.udp.server.use-epoll}")
    private boolean udpServerUseEpoll;

    @Value("${netty.udp.client.ip}")
    private String udpClientIp;

    @Value("${netty.udp.client.port}")
    private int udpClientPort;

    @Value("${netty.udp.client.use-epoll}")
    private boolean udpClientUseEpoll;

    @Override
    public void run(ApplicationArguments args) {
        NettyUtils.start(tcpServerIp, tcpServerPort, tcpServerUseEpoll, tcpClientIp, tcpClientPort, tcpClientUseEpoll, udpServerIp, udpServerPort, udpServerUseEpoll, udpClientIp, udpClientPort, udpClientUseEpoll);
        ThreadUtils.start();

    }
}
