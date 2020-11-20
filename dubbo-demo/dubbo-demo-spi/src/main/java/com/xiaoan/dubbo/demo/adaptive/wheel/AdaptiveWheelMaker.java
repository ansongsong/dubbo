package com.xiaoan.dubbo.demo.adaptive.wheel;

import com.xiaoan.dubbo.demo.adaptive.wheel.po.Wheel;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;

/**
 * @author ansongsong
 * @Description: WheelMaker 接口的自适应实现类
 *
 * AdaptiveWheelMaker 是一个代理类，与传统的代理逻辑不同，AdaptiveWheelMaker 所代理的对象 是在 makeWheel 方法中通过 SPI 加载得到的
 *
 * @date 2020/11/14 18:28
 */
public class AdaptiveWheelMaker implements WheelMaker{
    @Override
    public Wheel makeWheel(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("url == null");
        }

        // 1.从 URL 中获取 WheelMaker 名称
        String wheelMakerName = url.getParameter("Wheel.maker");
        if (wheelMakerName == null) {
            throw new IllegalArgumentException("wheelMakerName == null");
        }

        // 2.通过 SPI 加载具体的 WheelMaker
        WheelMaker wheelMaker = ExtensionLoader
                .getExtensionLoader(WheelMaker.class).getExtension(wheelMakerName);

        // 3.调用目标方法
        return wheelMaker.makeWheel(url);
    }
}
