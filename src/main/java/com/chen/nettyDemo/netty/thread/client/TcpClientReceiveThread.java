package com.chen.nettyDemo.netty.thread.client;

import com.chen.nettyDemo.netty.NettyUtils;
import com.chen.nettyDemo.netty.thread.QueueParams;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class TcpClientReceiveThread extends Thread {
    public static final LinkedBlockingDeque<QueueParams> recvQueue = new LinkedBlockingDeque<>();
    public static AtomicInteger index = new AtomicInteger(0);
    @Override
    public void run() {
        while (true) {
            try {
                QueueParams take = recvQueue.take();
                NettyUtils.sendServerResponse(take.getKey(), take.getData());
                System.out.println("发送Client前：" + index.getAndIncrement());
            } catch (Exception e) {
                log.error("TCP Client Receive Take异常， " + e.getMessage());
            }
        }
    }

    public void putQueue(QueueParams data) {
        try {
            recvQueue.put(data);
        } catch (Exception e) {
            log.error("TCP Client Receive Put异常，" + e.getMessage());
        }
    }
}
