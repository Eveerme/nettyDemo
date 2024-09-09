package com.chen.nettyDemo.netty.thread;

import com.chen.nettyDemo.netty.thread.client.TcpClientReceiveThread;
import com.chen.nettyDemo.netty.thread.client.TcpClientSendThread;
import com.chen.nettyDemo.netty.thread.client.UdpClientReceiveThread;
import com.chen.nettyDemo.netty.thread.client.UdpClientSendThread;
import com.chen.nettyDemo.netty.thread.server.TcpServerReceiveThread;
import com.chen.nettyDemo.netty.thread.server.TcpServerSendThread;
import com.chen.nettyDemo.netty.thread.server.UdpServerReceiveThread;
import com.chen.nettyDemo.netty.thread.server.UdpServerSendThread;

public class ThreadUtils {

    public static TcpServerSendThread tcpServerSend = new TcpServerSendThread();
    public static TcpClientSendThread tcpClientSend = new TcpClientSendThread();
    public static UdpServerSendThread udpServerSend = new UdpServerSendThread();
    public static UdpClientSendThread udpClientSend = new UdpClientSendThread();

    public static TcpServerReceiveThread tcpServerReceive = new TcpServerReceiveThread();
    public static TcpClientReceiveThread tcpClientReceive = new TcpClientReceiveThread();
    public static UdpServerReceiveThread udpServerReceive = new UdpServerReceiveThread();
    public static UdpClientReceiveThread udpClientReceive = new UdpClientReceiveThread();

    public static void start() {
        tcpServerReceive.start();
        tcpClientReceive.start();
        udpServerReceive.start();
        udpClientReceive.start();

        tcpServerSend.start();
        tcpClientSend.start();
        udpServerSend.start();
        udpClientSend.start();
    }
}
