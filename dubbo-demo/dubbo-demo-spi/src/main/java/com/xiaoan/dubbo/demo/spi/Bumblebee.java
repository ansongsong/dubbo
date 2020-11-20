package com.xiaoan.dubbo.demo.spi;

/**
 * @author ansongsong
 * @Description: 大黄蜂 机器人
 * @date 2020/11/14 11:06
 */
public class Bumblebee implements Robot {
    @Override
    public void sayHello() {
        System.out.println("Hello, I am Bumblebee.");
    }
}
