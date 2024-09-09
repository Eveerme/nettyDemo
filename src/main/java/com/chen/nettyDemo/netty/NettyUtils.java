package com.chen.nettyDemo.netty;

import com.chen.nettyDemo.netty.server.TcpServer;
import com.chen.nettyDemo.netty.server.UdpServer;
import com.chen.nettyDemo.netty.client.TcpClient;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class NettyUtils {

    public static String tcpClientip;

    public static int tcpClientport;

    public static boolean tcpClientuseEpoll;

    public static String udpClientip;

    public static int udpClientport;

    public static boolean udpClientuseEpoll;

    public final static TcpServer tcpServer = new TcpServer();
    public final static UdpServer udpServer = new UdpServer();

    public static ConcurrentHashMap<String, Channel> tcpServerChannelMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Channel> tcpClientChannelMap = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, Channel> udpServerChannelMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Channel> udpClientChannelMap = new ConcurrentHashMap<>();

    public static void start(String ip1, int port1, boolean use1, String ip2, int port2, boolean use2, String ip3, int port3, boolean use3, String ip4, int port4, boolean use4) {
        new Thread(() -> tcpServer.start(ip1, port1, use1)).start();
        new Thread(() -> udpServer.start(ip3, port3, use3)).start();
        tcpClientip = ip2;
        tcpClientport = port2;
        tcpClientuseEpoll = use2;
        udpClientip = ip4;
        udpClientport = port4;
        udpClientuseEpoll = use4;
    }

    public static void startClientConnect(String ip, int port, boolean isUseEpoll, String key) {
        TcpClient tcpClient = new TcpClient();
        tcpClient.start(ip, port, isUseEpoll, key);
    }

    public static void sendClientResponse(String key, byte[] msg) {
        log.info("发送Tcp Client消息，{}" + key);
        Channel channel = tcpClientChannelMap.get(key);
        channel.writeAndFlush(Unpooled.copiedBuffer(msg));
    }

    public static void setServerChannelAutoRead(String key) {
        log.info("打开Tcp Server接收消息，{}" + key);
        Channel channel = tcpServerChannelMap.get(key);
        channel.config().setAutoRead(true);
    }

    public static void sendServerResponse(String key, byte[] msg) {
        log.info("发送Tcp Server消息，{}" + key);
        Channel channel = tcpServerChannelMap.get(key);
        channel.writeAndFlush(Unpooled.copiedBuffer(msg));
    }
}
