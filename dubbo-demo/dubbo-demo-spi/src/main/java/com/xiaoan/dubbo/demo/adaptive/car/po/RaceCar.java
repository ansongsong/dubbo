package com.xiaoan.dubbo.demo.adaptive.car.po;

import com.xiaoan.dubbo.demo.adaptive.wheel.po.Wheel;

/**
 * @author ansongsong
 * @Description:
 * @date 2020/11/14 18:39
 */
public class RaceCar extends Car{

    public RaceCar(Wheel wheel) {
        this.wheel = wheel;
    }
}
