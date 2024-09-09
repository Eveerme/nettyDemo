package com.chen.nettyDemo.netty.thread.client;

import com.chen.nettyDemo.netty.thread.QueueParams;
import com.chen.nettyDemo.netty.NettyUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingDeque;


@Slf4j
public class TcpClientReceiveThread extends Thread {
    public static final LinkedBlockingDeque<QueueParams> recvQueue = new LinkedBlockingDeque<>();
    @Override
    public void run() {
        while (true) {
            try {
                QueueParams take = recvQueue.take();
                NettyUtils.sendServerResponse(take.getKey(), take.getData());
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
