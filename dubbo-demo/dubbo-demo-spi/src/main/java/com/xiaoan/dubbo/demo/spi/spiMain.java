package com.xiaoan.dubbo.demo.spi;

import com.xiaoan.dubbo.demo.spi.Robot;
import org.apache.dubbo.common.extension.ExtensionLoader;

/**
 * @author ansongsong
 * @Description:
 * @date 2020/11/14 13:13
 */
public class spiMain {
    public static void main(String[] args) {
        sayHello();
    }


    public static void sayHello() {
        // 首先通过 ExtensionLoader 的 getExtensionLoader 方法获取一个 ExtensionLoader 实例
        ExtensionLoader<Robot> extensionLoader =
                ExtensionLoader.getExtensionLoader(Robot.class);
        // 然后再通过 ExtensionLoader 的 getExtension 方法获取拓展类对象
        Robot optimusPrime = extensionLoader.getExtension("optimusPrime");
        optimusPrime.sayHello();
        Robot bumblebee = extensionLoader.getExtension("bumblebee");
        bumblebee.sayHello();
    }
}
