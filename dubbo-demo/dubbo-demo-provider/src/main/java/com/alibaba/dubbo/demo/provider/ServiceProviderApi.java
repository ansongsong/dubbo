package com.alibaba.dubbo.demo.provider;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.demo.DemoService;

/**
 * API 配置 Provider
 * @Author: ansongsong
 * @Date: 2019/11/22
 */
public class ServiceProviderApi {


    public static void main(String[] args) {
        // 服务实现
        DemoService demoService = new DemoServiceImpl();

        // 当前应用配置
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("demoService");

        // 连接注册中心配置
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("192.168.10.23:9090");
        registryConfig.setUsername("root");
        registryConfig.setPassword("123456");

        //服务提供者协议配置
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(100);
        protocolConfig.setThreads(200);

        // 注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口
        // 服务提供者暴露服务配置 // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
        ServiceConfig<DemoService> serviceConfig = new ServiceConfig<DemoService>();
        serviceConfig.setApplication(applicationConfig);
        serviceConfig.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
        serviceConfig.setProtocol(protocolConfig);// 多个协议可以用setProtocols()
        serviceConfig.setInterface(DemoService.class);
        serviceConfig.setRef(demoService);
        serviceConfig.setVersion("1.0.0");

        // 暴露及注册服务
        serviceConfig.export();
    }

}
