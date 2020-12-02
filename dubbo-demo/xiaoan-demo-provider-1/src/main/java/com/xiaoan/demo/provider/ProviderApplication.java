package com.xiaoan.demo.provider;

import com.xiaoan.demo.client.service.GreetingsService;
import com.xiaoan.demo.provider.service.impl.GreetingsServiceImpl;
import org.apache.dubbo.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.service.ServiceConfig;

import java.util.concurrent.CountDownLatch;

public class ProviderApplication {
    private static String zookeeperHost = System.getProperty("zookeeper.address", "127.0.0.1");

    public static void main(String[] args) throws Exception {
//        ServiceConfig<GreetingsService> service = new ServiceConfig<>();
//        service.setApplication(new ApplicationConfig("first-dubbo-provider"));
//        service.setRegistry(new RegistryConfig("zookeeper://" + zookeeperHost + ":2181"));
//        service.setInterface(GreetingsService.class);
//        service.setRef(new GreetingsServiceImpl());
//        service.export();
//
//        System.out.println("dubbo service started");
//        new CountDownLatch(1).await();

        ProtocolConfig dubbo = new ProtocolConfig("dubbo", 20880);
        dubbo.setHost("127.0.0.1");


        ServiceConfig<GreetingsService> service = new ServiceConfig<GreetingsService>();
        service.setInterface(GreetingsService.class);
        service.setRef(new GreetingsServiceImpl());
        service.setRegistry(new RegistryConfig("zookeeper://" + zookeeperHost + ":2181"));
        service.setProtocol(dubbo);



        DubboBootstrap bootstrap = DubboBootstrap.getInstance()
                .application(new ApplicationConfig("first-dubbo-provider"))
//                .protocol(dubbo)
                .registry(new RegistryConfig("zookeeper://" + zookeeperHost + ":2181"))
                .service(service)
                .start();

        System.out.println("xxxxxxxxxxx provider start xxxxxxxxxxxxxxxx");
        new CountDownLatch(1).await();
    }
}
