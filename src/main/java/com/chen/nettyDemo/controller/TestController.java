package com.chen.nettyDemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname TestController
 * @Description TODO
 * @Date 2024/9/10 下午3:49
 * @Created by chen_ytao
 */
@RestController
public class TestController {
    @GetMapping("/")
    public String get() {
        System.out.println("hello");
        return "Hello";
    }
}
