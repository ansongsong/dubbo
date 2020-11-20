package com.xiaoan.dubbo.demo.adaptive.car;

import com.xiaoan.dubbo.demo.adaptive.car.po.Car;
import com.xiaoan.dubbo.demo.adaptive.car.po.RaceCar;
import com.xiaoan.dubbo.demo.adaptive.wheel.po.Wheel;
import com.xiaoan.dubbo.demo.adaptive.wheel.WheelMaker;
import org.apache.dubbo.common.URL;

/**
 * @author ansongsong
 * @Description: 竞赛汽车制造工厂
 * @date 2020/11/14 18:32
 */
public class RaceCarMaker implements CarMaker  {
    // RaceCarMaker 持有一个 WheelMaker 类型的成员变量
    // 在程序启动时，我们可以将 AdaptiveWheelMaker 通过 setter 方法注入到 RaceCarMaker 中
    WheelMaker wheelMaker;

    // 通过 setter 注入 AdaptiveWheelMaker
    public void setWheelMaker(WheelMaker wheelMaker) {
        this.wheelMaker = wheelMaker;
    }
    @Override
    public Car makeCar(URL url) {
        Wheel wheel = wheelMaker.makeWheel(url);
        return new RaceCar(wheel);
    }
}
