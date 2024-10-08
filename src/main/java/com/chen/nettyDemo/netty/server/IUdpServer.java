package com.chen.nettyDemo.netty.server;

import javax.annotation.PreDestroy;

public interface IUdpServer {


    /**
     * 主启动程序，初始化参数
     *
     * @throws Exception 初始化异常
     */
    void start(String ip, int port, boolean isUseEpoll) throws Exception;


    /**
     * 优雅的结束服务器
     *
     * @throws InterruptedException 提前中断异常
     */
    @PreDestroy
    void destroy() throws InterruptedException;
}
