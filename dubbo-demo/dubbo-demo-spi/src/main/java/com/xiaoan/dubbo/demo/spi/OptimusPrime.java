package com.xiaoan.dubbo.demo.spi;

/**
 * @author ansongsong
 * @Description: 擎天柱 机器人
 * @date 2020/11/14 11:06
 */
public class OptimusPrime implements Robot {
    @Override
    public void sayHello() {
        System.out.println("Hello, I am Optimus Prime.");
    }
}
