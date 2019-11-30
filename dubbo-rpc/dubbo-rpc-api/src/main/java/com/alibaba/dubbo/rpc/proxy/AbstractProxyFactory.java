/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.rpc.proxy;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.utils.ReflectUtils;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.ProxyFactory;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.service.EchoService;
import com.alibaba.dubbo.rpc.service.GenericService;

/**
 * AbstractProxyFactory
 */
public abstract class AbstractProxyFactory implements ProxyFactory {

    @Override
    public <T> T getProxy(Invoker<T> invoker) throws RpcException {
        return getProxy(invoker, false);
    }

    @Override
    public <T> T getProxy(Invoker<T> invoker, boolean generic) throws RpcException {
        Class<?>[] interfaces = null;
        // url : registry://192.168.0.197:2181/com.alibaba.dubbo.registry.RegistryService?application=xianzhi_admin_consumer&cluster=available&dubbo=2.5.3&pid=25436&refer=application%3Dxianzhi_admin_consumer%26dubbo%3D2.5.3%26interface%3Dcom.xianzhi.apis.coupon.CouponClientApi%26methods%3DautoSendCouponByPkgId%2CfrozenCoupon%2CeditCouponLangs%2CconsumeCouponCL%2CconsumeCoupon%2CexchangeAutoByType%26monitor%3Ddubbo%253A%252F%252F192.168.0.197%253A2181%252Fcom.alibaba.dubbo.registry.RegistryService%253Fapplication%253Dxianzhi_admin_consumer%2526dubbo%253D2.5.3%2526pid%253D25436%2526protocol%253Dregistry%2526refer%253Ddubbo%25253D2.5.3%252526interface%25253Dcom.alibaba.dubbo.monitor.MonitorService%252526pid%25253D25436%252526timestamp%25253D1574912187698%2526registry%253Dzookeeper%2526timestamp%253D1574912187695%26pid%3D25436%26revision%3D14.13%26side%3Dconsumer%26timestamp%3D1574912187689&registry=zookeeper&timestamp=1574912187695
        String config = invoker.getUrl().getParameter("interfaces");
        if (config != null && config.length() > 0) {
            String[] types = Constants.COMMA_SPLIT_PATTERN.split(config);
            if (types != null && types.length > 0) {
                interfaces = new Class<?>[types.length + 2];
                interfaces[0] = invoker.getInterface();
                interfaces[1] = EchoService.class;
                for (int i = 0; i < types.length; i++) {
                    interfaces[i + 1] = ReflectUtils.forName(types[i]);
                }
            }
        }
        // 增加 EchoService 接口，用于回生测试。参见文档《回声测试》http://dubbo.apache.org/zh-cn/docs/user/demos/echo-service.html
        if (interfaces == null) {
            // invoker.getInterface = interface com.xianzhi.apis.coupon.CouponClientApi
            interfaces = new Class<?>[]{invoker.getInterface(), EchoService.class};
        }

        if (!invoker.getInterface().equals(GenericService.class) && generic) {
            int len = interfaces.length;
            Class<?>[] temp = interfaces;
            interfaces = new Class<?>[len + 1];
            System.arraycopy(temp, 0, interfaces, 0, len);
            interfaces[len] = GenericService.class;
        }

        return getProxy(invoker, interfaces);
    }

    public abstract <T> T getProxy(Invoker<T> invoker, Class<?>[] types);

}
