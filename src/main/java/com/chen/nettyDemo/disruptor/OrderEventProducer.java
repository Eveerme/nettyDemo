package com.chen.nettyDemo.disruptor;

import com.lmax.disruptor.RingBuffer;

/**
 * 定义生产者：生产者负责将订单事件发布到 Disruptor的RingBuffer中
 */
public class OrderEventProducer {
    private final RingBuffer<OrderEvent> ringBuffer;

    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(String key, byte[] data) {
        long sequence = ringBuffer.next();  // 获取下一个序列号
        try {
            OrderEvent orderEvent = ringBuffer.get(sequence); // 获取事件对象
            orderEvent.setKey(key);
            orderEvent.setData(data);

        } finally {
            ringBuffer.publish(sequence);  // 发布事件
        }
    }
}
