package com.xiaoan.dubbo.demo.spi;

import org.apache.dubbo.common.extension.SPI;

/**
 * @author ansongsong
 * @Description:
 *
 * 接口 千万不要忘记加注解 @SPI
 * @date 2020/11/12 17:31
 */

@SPI
public interface Robot {
    void sayHello();
}
