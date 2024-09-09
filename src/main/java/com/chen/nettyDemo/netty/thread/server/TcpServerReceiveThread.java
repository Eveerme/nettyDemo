package com.chen.nettyDemo.netty.thread.server;

import com.chen.nettyDemo.netty.NettyUtils;
import com.chen.nettyDemo.netty.thread.QueueParams;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingDeque;


@Slf4j
public class TcpServerReceiveThread extends Thread {
    public static final LinkedBlockingDeque<QueueParams> recvQueue = new LinkedBlockingDeque<>();
    @Override
    public void run() {
        while (true) {
            try {
                QueueParams take = recvQueue.take();
                NettyUtils.sendClientResponse(take.getKey(), take.getData());
            } catch (Exception e) {
                log.error("Server Receive Take异常， " + e.getMessage());
            }
        }
    }

    public void putQueue(QueueParams data) {
        try {
            recvQueue.put(data);
        } catch (Exception e) {
            log.error("Server Receive Put异常，" + e.getMessage());
        }
    }
}
