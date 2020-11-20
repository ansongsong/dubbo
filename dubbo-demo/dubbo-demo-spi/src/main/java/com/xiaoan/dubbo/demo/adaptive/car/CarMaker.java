package com.xiaoan.dubbo.demo.adaptive.car;

import com.xiaoan.dubbo.demo.adaptive.car.po.Car;
import org.apache.dubbo.common.URL;

/**
 * @author ansongsong
 * @Description: 汽车制造工厂
 * @date 2020/11/14 18:31
 */
public interface CarMaker {
    Car makeCar(URL url);
}
