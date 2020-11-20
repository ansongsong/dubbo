package com.xiaoan.dubbo.demo.adaptive.wheel;

import com.xiaoan.dubbo.demo.adaptive.wheel.po.Wheel;
import org.apache.dubbo.common.URL;

/**
 * @author ansongsong
 * @Description: 车轮制造工厂
 * @date 2020/11/14 18:27
 */
public interface WheelMaker {
    Wheel makeWheel(URL url);
}
