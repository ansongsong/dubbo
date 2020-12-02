package com.xiaoan.demo.consumer;

import com.xiaoan.demo.client.service.GreetingsService;
import org.apache.dubbo.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.MetadataReportConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.service.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

public class ConsumerApplication {
    private static String zookeeperHost = System.getProperty("zookeeper.address", "127.0.0.1");


    public static void main(String[] args) {
        ReferenceConfig<GreetingsService> reference = new ReferenceConfig<>();
//        reference.setApplication(new ApplicationConfig("first-dubbo-consumer"));
        reference.setRegistry(new RegistryConfig("zookeeper://" + zookeeperHost + ":2181"));
        reference.setInterface(GreetingsService.class);
        reference.setRetries(1);
        reference.setTimeout(30*1000);
        // org.apache.dubbo.rpc.cluster.support.AbstractClusterInvoker.select
        // sticky 表示粘滞连接。所谓粘滞连接是指让服务消费者尽可能的 调用同一个服务提供者，除非该提供者挂了再进行切换
//        reference.setSticky();
//        reference.setCluster();
        DubboBootstrap bootstrap = DubboBootstrap.getInstance()
                .application(new ApplicationConfig("consumer"))
                .registry(new RegistryConfig("zookeeper://127.0.0.1:2181"))
                .protocol(new ProtocolConfig("dubbo",20880))
//                .metadataReport(new MetadataReportConfig("zookeeper://127.0.0.1:2181"))
                .reference(reference)
                .start();

        GreetingsService service = bootstrap.getCache().get(reference);
        String message = service.sayHi("dubbo");
        System.err.println(message);
    }

}
