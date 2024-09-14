package com.chen.nettyDemo.disruptor;


import com.lmax.disruptor.EventFactory;

/**
 * 定义事件工厂
 */
public class OrderEventFactory implements EventFactory<OrderEvent> {
    @Override
    public OrderEvent newInstance() {
        return new OrderEvent();
    }
}
