package com.xiaoan.adpative;

import com.alibaba.dubbo.common.extension.ExtensionLoader;

public class Protocol$Adpative implements com.alibaba.dubbo.rpc.Protocol {

    public void destroy() {
        throw new UnsupportedOperationException("method public abstract void com.alibaba.dubbo.rpc.Protocol.destroy() of interface com.alibaba.dubbo.rpc.Protocol is not adaptive method!");
    }

    public int getDefaultPort() {
        throw new UnsupportedOperationException("method public abstract int com.alibaba.dubbo.rpc.Protocol.getDefaultPort() of interface com.alibaba.dubbo.rpc.Protocol is not adaptive method!");
    }

    public com.alibaba.dubbo.rpc.Exporter export(com.alibaba.dubbo.rpc.Invoker arg0) throws com.alibaba.dubbo.rpc.Invoker {
        if (arg0 == null)
            throw new IllegalArgumentException("com.alibaba.dubbo.rpc.Invoker argument == null");
        if (arg0.getUrl() == null)
            throw new IllegalArgumentException("com.alibaba.dubbo.rpc.Invoker argument getUrl() == null");
        com.alibaba.dubbo.common.URL url = arg0.getUrl();
        String extName = (url.getProtocol() == null ? "dubbo" : url.getProtocol());
        if (extName == null)
            throw new IllegalStateException("Fail to get extension(com.alibaba.dubbo.rpc.Protocol) name from url(" + url.toString() + ") use keys([protocol])");
        com.alibaba.dubbo.rpc.Protocol extension = (com.alibaba.dubbo.rpc.Protocol) ExtensionLoader.getExtensionLoader(com.alibaba.dubbo.rpc.Protocol.class).getExtension(extName);
        return extension.export(arg0);
    }

    /**
     *
     * @param arg0 interface com.xianzhi.apis.xiamendeposit.XiamenDepositApi
     * @param arg1 registry://192.168.0.197:2181/com.alibaba.dubbo.registry.RegistryService?application=xianzhi_admin_consumer&dubbo=2.5.3&pid=25436&refer=application%3Dxianzhi_admin_consumer%26check%3Dfalse%26dubbo%3D2.5.3%26interface%3Dcom.xianzhi.apis.xiamendeposit.XiamenDepositApi%26methods%3DqueryTransaction%2CfundTransferredOut%2CcgtQueryTransBatch%2CqueryDepositProductInfor%2CqueryDepositUserInfor%2CmodifyCgtProductStatus%2CqueryPlatformInfor%2CsyncTransaction%26monitor%3Ddubbo%253A%252F%252F192.168.0.197%253A2181%252Fcom.alibaba.dubbo.registry.RegistryService%253Fapplication%253Dxianzhi_admin_consumer%2526dubbo%253D2.5.3%2526pid%253D25436%2526protocol%253Dregistry%2526refer%253Ddubbo%25253D2.5.3%252526interface%25253Dcom.alibaba.dubbo.monitor.MonitorService%252526pid%25253D25436%252526timestamp%25253D1574857342514%2526registry%253Dzookeeper%2526timestamp%253D1574857342514%26pid%3D25436%26revision%3D14.13%26side%3Dconsumer%26timestamp%3D1574857342488&registry=zookeeper&timestamp=1574857342514
     * @return
     * @throws java.lang.Class
     */
    @Override
    public com.alibaba.dubbo.rpc.Invoker refer(java.lang.Class arg0, com.alibaba.dubbo.common.URL arg1) throws java.lang.Class {
        if (arg1 == null)
            throw new IllegalArgumentException("url == null");
        com.alibaba.dubbo.common.URL url = arg1;
        String extName = (url.getProtocol() == null ? "dubbo" : url.getProtocol());
        if (extName == null)
            throw new IllegalStateException("Fail to get extension(com.alibaba.dubbo.rpc.Protocol) name from url(" + url.toString() + ") use keys([protocol])");
        com.alibaba.dubbo.rpc.Protocol extension = (com.alibaba.dubbo.rpc.Protocol) ExtensionLoader.getExtensionLoader(com.alibaba.dubbo.rpc.Protocol.class).getExtension(extName);
        return extension.refer(arg0, arg1);
    }
}


