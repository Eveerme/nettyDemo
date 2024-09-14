package com.chen.nettyDemo.netty.thread.server;

import com.chen.nettyDemo.netty.thread.QueueParams;
import com.chen.nettyDemo.netty.thread.ThreadUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class TcpServerSendThread extends Thread {
    public static final LinkedBlockingDeque<QueueParams> sendQueue = new LinkedBlockingDeque<>();

    public static AtomicInteger index = new AtomicInteger(0);
    @Override
    public void run() {
        while (true) {
            try {
                QueueParams take = sendQueue.take();
                ThreadUtils.tcpServerReceive.putQueue(take);
                System.out.println("跨网发送前：" + index.getAndIncrement());
            } catch (Exception e) {
                log.error("TCP Server Send Take异常， " + e.getMessage());
            }
        }
    }

    public void putQueue(QueueParams data) {
        try {
            sendQueue.put(data);
        } catch (Exception e) {
            log.error("TCP Server Send Put异常，" + e.getMessage());
        }
    }
}
