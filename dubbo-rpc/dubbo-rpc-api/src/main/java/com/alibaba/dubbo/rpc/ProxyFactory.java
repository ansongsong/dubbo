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
package com.alibaba.dubbo.rpc;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;

/**
 * 代理工厂接口
 * ProxyFactory. (API/SPI, Singleton, ThreadSafe)
 */
@SPI("javassist")
public interface ProxyFactory {

    /**
     * create proxy.
     * 创建 Proxy ，在引用服务调用。（ps: 消费者中调用）
     * @param invoker  Consumer 对 Provider 调用的 Invoker
     * @return proxy
     */
    @Adaptive({Constants.PROXY_KEY})
    <T> T getProxy(Invoker<T> invoker) throws RpcException;

    /**
     * create proxy.
     *
     * @param invoker
     * @return proxy
     */
    @Adaptive({Constants.PROXY_KEY})
    <T> T getProxy(Invoker<T> invoker, boolean generic) throws RpcException;

    /**
     * create invoker.
     * 创建 Invoker ，在暴露服务时调用。（ps: 服务者中调用）
     * @param proxy Service 对象。   articleServiceApiImpl
     * @param type Service 接口类型。  interface com.xianzhi.apis.search.ArticleServiceApi
     * @param url Service 对应的 Dubbo URL 。 registry://127.0.0.1:2181/com.alibaba.dubbo.registry.RegistryService?application=xianzhi-search&dubbo=2.5.3&export=dubbo%3A%2F%2F192.168.10.28%3A20880%2Fcom.xianzhi.apis.search.ArticleServiceApi%3Fanyhost%3Dtrue%26application%3Dxianzhi-search%26dubbo%3D2.5.3%26interface%3Dcom.xianzhi.apis.search.ArticleServiceApi%26methods%3Doffline%2CgetTitleByWordPage%2Csave%2Cupdate%2Conline%2CbulkUpdate%2CgetArticlesAndPage%2CupdateFileds%2Cdelete%2CfindByQueryParams%26pid%3D67648%26revision%3D11.8%26side%3Dprovider%26timestamp%3D1574751703678&pid=67648&registry=zookeeper&timestamp=1574751703663
     * @param <T>
     * @return
     * @throws RpcException
     */
    @Adaptive({Constants.PROXY_KEY})
    <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) throws RpcException;

}
