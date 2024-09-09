package com.chen.nettyDemo.netty.thread;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueueParams {
    private String key;
    private byte[] data;
}
