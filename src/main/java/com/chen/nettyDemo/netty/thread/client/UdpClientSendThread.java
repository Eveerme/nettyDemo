package com.chen.nettyDemo.netty.thread.client;

import com.chen.nettyDemo.netty.thread.QueueParams;
import com.chen.nettyDemo.netty.thread.ThreadUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingDeque;

@Slf4j
public class UdpClientSendThread extends Thread {
    public static final LinkedBlockingDeque<QueueParams> sendQueue = new LinkedBlockingDeque<>();
    @Override
    public void run() {
        while (true) {
            try {
                QueueParams take = sendQueue.take();
                ThreadUtils.udpClientReceive.putQueue(take);
            } catch (Exception e) {
                log.error("UDP Client Send Take异常， " + e.getMessage());
            }
        }
    }
    public void putQueue(QueueParams data) {
        try {
            sendQueue.put(data);
        } catch (Exception e) {
            log.error("UDP Client Send Put异常，" + e.getMessage());
        }
    }

}
