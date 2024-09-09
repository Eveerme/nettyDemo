package com.chen.nettyDemo.netty.thread.server;

import com.chen.nettyDemo.netty.NettyUtils;
import com.chen.nettyDemo.netty.client.UdpClient;
import com.chen.nettyDemo.netty.thread.QueueParams;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingDeque;


@Slf4j
public class UdpServerReceiveThread extends Thread {
    public static final LinkedBlockingDeque<QueueParams> recvQueue = new LinkedBlockingDeque<>();
    @Override
    public void run() {
        while (true) {
            try {
                QueueParams take = recvQueue.take();
                Channel channel = NettyUtils.udpClientChannelMap.get(take.getKey());
                if (channel == null) {
                    UdpClient udpClient = new UdpClient();
                    udpClient.connectAndSendMsg(take.getKey(), take.getData());
                } else {
                    channel.writeAndFlush(take.getData());
                }
            } catch (Exception e) {
                log.error("UDP Server Receive Take异常， " + e.getMessage());
            }
        }
    }

    public void putQueue(QueueParams data) {
        try {
            recvQueue.put(data);
        } catch (Exception e) {
            log.error("UDP Server Receive Put异常，" + e.getMessage());
        }
    }
}
