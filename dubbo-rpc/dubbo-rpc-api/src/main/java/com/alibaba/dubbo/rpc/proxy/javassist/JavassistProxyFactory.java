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
package com.alibaba.dubbo.rpc.proxy.javassist;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.bytecode.Proxy;
import com.alibaba.dubbo.common.bytecode.Wrapper;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.proxy.AbstractProxyFactory;
import com.alibaba.dubbo.rpc.proxy.AbstractProxyInvoker;
import com.alibaba.dubbo.rpc.proxy.InvokerInvocationHandler;

/**
 * 基于 Javassist 代理工厂实现类
 * JavaassistRpcProxyFactory
 */
public class JavassistProxyFactory extends AbstractProxyFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Invoker<T> invoker, Class<?>[] interfaces) {
        return (T) Proxy.getProxy(interfaces).newInstance(new InvokerInvocationHandler(invoker));
    }

    /**
     *
     * @param proxy Service 对象。   articleServiceApiImpl
     * @param type Service 接口类型。  interface com.xianzhi.apis.search.ArticleServiceApi
     * @param url Service 对应的 Dubbo URL 。 registry://127.0.0.1:2181/com.alibaba.dubbo.registry.RegistryService?application=xianzhi-search&dubbo=2.5.3&export=dubbo%3A%2F%2F192.168.10.28%3A20880%2Fcom.xianzhi.apis.search.ArticleServiceApi%3Fanyhost%3Dtrue%26application%3Dxianzhi-search%26dubbo%3D2.5.3%26interface%3Dcom.xianzhi.apis.search.ArticleServiceApi%26methods%3Doffline%2CgetTitleByWordPage%2Csave%2Cupdate%2Conline%2CbulkUpdate%2CgetArticlesAndPage%2CupdateFileds%2Cdelete%2CfindByQueryParams%26pid%3D67648%26revision%3D11.8%26side%3Dprovider%26timestamp%3D1574751703678&pid=67648&registry=zookeeper&timestamp=1574751703663
     * @param <T>
     * @return
     * @throws RpcException
     */
    @Override
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) {
        // TODO Wrapper cannot handle this scenario correctly: the classname contains '$'
        // TODO Wrapper类不能正确处理带$的类名
        // proxy.getClass().getName().indexOf('$') = -1
        final Wrapper wrapper = Wrapper.getWrapper(proxy.getClass().getName().indexOf('$') < 0 ? proxy.getClass() : type);
        return new AbstractProxyInvoker<T>(proxy, type, url) {
            @Override
            protected Object doInvoke(T proxy, String methodName,
                                      Class<?>[] parameterTypes,
                                      Object[] arguments) throws Throwable {
                return wrapper.invokeMethod(proxy, methodName, parameterTypes, arguments);
            }
        };
    }

}
