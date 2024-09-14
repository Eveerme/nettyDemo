package com.chen.nettyDemo.disruptor;


import lombok.Data;

/**
 * 定义订单事件
 */
@Data
public class OrderEvent {
    private String key;
    private byte[] data;
}
