package com.chen.nettyDemo.disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * 定义事件处理器：实际处理事件的地方
 */
public class OrderEventHandler implements EventHandler<OrderEvent> {

    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) {
        // 处理事件的具体业务逻辑
        System.out.println("TCP 处理事件: " + event.getKey());
    }

}
