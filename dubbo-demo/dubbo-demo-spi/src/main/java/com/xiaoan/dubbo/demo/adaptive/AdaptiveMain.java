package com.xiaoan.dubbo.demo.adaptive;

import com.xiaoan.dubbo.demo.adaptive.car.RaceCarMaker;
import com.xiaoan.dubbo.demo.adaptive.wheel.AdaptiveWheelMaker;
import org.apache.dubbo.common.URL;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ansongsong
 * @Description:
 * @date 2020/11/14 18:42
 */
public class AdaptiveMain {
    public static void main(String[] args) {
        RaceCarMaker raceCarMaker = new RaceCarMaker();
        //通过 setter 注入 AdaptiveWheelMaker
        raceCarMaker.setWheelMaker(new AdaptiveWheelMaker());
        // 组拼 URL
        Map<String, String> parameters = new HashMap<>();
        parameters.put("wheel.maker","MichelinWheelMaker");
        URL url = new URL("dubbo","192.168.0.101",20880,"/XxxService",parameters);
        // 调用制作汽车，最后将url 传入到 AdaptiveWheelMaker 中
        raceCarMaker.makeCar(url);

    }




}
