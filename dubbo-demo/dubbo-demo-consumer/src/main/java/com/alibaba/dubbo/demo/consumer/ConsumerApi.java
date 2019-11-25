package com.alibaba.dubbo.demo.consumer;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.demo.DemoService;

/**
 * API 配置 Consumer
 * @Author: ansongsong
 * @Date: 2019/11/23
 */
public class ConsumerApi {
    public static void main(String[] args) {
        // 当前应用配置
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("demoService");

        // 链接注册中心配置
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("192.168.10.3:9090");
        registryConfig.setUsername("aa");
        registryConfig.setPassword("bb");

        // 注意：ReferenceConfig为重对象，内部封装了与注册中心的连接，以及与服务提供方的连接

        // 引用远程服务
        ReferenceConfig<DemoService> referenceConfig = new ReferenceConfig<DemoService>();
        // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
        referenceConfig.setApplication(applicationConfig);
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setInterface(DemoService.class);
        referenceConfig.setVersion("1.0.0");

        // 和本地bean一样使用xxxService // 注意：此代理对象内部封装了所有通讯细节，对象较重，请缓存复用
        DemoService demoService = referenceConfig.get();


    }
}
