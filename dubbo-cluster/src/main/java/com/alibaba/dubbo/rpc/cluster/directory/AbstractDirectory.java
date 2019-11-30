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
package com.alibaba.dubbo.rpc.cluster.directory;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.cluster.Directory;
import com.alibaba.dubbo.rpc.cluster.Router;
import com.alibaba.dubbo.rpc.cluster.RouterFactory;
import com.alibaba.dubbo.rpc.cluster.router.MockInvokersSelector;
import com.alibaba.dubbo.rpc.cluster.router.tag.TagRouter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract implementation of Directory: Invoker list returned from this Directory's list method have been filtered by Routers
 *
 */
public abstract class AbstractDirectory<T> implements Directory<T> {

    // logger
    private static final Logger logger = LoggerFactory.getLogger(AbstractDirectory.class);
    // zookeeper://192.168.0.197:2181/com.alibaba.dubbo.registry.RegistryService?application=xianzhi_admin_consumer&dubbo=2.5.3&pid=25436&refer=application%3Dxianzhi_admin_consumer%26check%3Dfalse%26dubbo%3D2.5.3%26interface%3Dcom.xianzhi.apis.xiamendeposit.XiamenDepositApi%26methods%3DqueryTransaction%2CfundTransferredOut%2CcgtQueryTransBatch%2CqueryDepositProductInfor%2CqueryDepositUserInfor%2CmodifyCgtProductStatus%2CqueryPlatformInfor%2CsyncTransaction%26monitor%3Ddubbo%253A%252F%252F192.168.0.197%253A2181%252Fcom.alibaba.dubbo.registry.RegistryService%253Fapplication%253Dxianzhi_admin_consumer%2526dubbo%253D2.5.3%2526pid%253D25436%2526protocol%253Dregistry%2526refer%253Ddubbo%25253D2.5.3%252526interface%25253Dcom.alibaba.dubbo.monitor.MonitorService%252526pid%25253D25436%252526timestamp%25253D1574857342514%2526registry%253Dzookeeper%2526timestamp%253D1574857342514%26pid%3D25436%26revision%3D14.13%26side%3Dconsumer%26timestamp%3D1574857342488&timestamp=1574857342514
    private final URL url;

    private volatile boolean destroyed = false;
    // zookeeper://192.168.0.197:2181/com.alibaba.dubbo.registry.RegistryService?application=xianzhi_admin_consumer&dubbo=2.5.3&pid=25436&refer=application%3Dxianzhi_admin_consumer%26check%3Dfalse%26dubbo%3D2.5.3%26interface%3Dcom.xianzhi.apis.xiamendeposit.XiamenDepositApi%26methods%3DqueryTransaction%2CfundTransferredOut%2CcgtQueryTransBatch%2CqueryDepositProductInfor%2CqueryDepositUserInfor%2CmodifyCgtProductStatus%2CqueryPlatformInfor%2CsyncTransaction%26monitor%3Ddubbo%253A%252F%252F192.168.0.197%253A2181%252Fcom.alibaba.dubbo.registry.RegistryService%253Fapplication%253Dxianzhi_admin_consumer%2526dubbo%253D2.5.3%2526pid%253D25436%2526protocol%253Dregistry%2526refer%253Ddubbo%25253D2.5.3%252526interface%25253Dcom.alibaba.dubbo.monitor.MonitorService%252526pid%25253D25436%252526timestamp%25253D1574857342514%2526registry%253Dzookeeper%2526timestamp%253D1574857342514%26pid%3D25436%26revision%3D14.13%26side%3Dconsumer%26timestamp%3D1574857342488&timestamp=1574857342514
    //consumer://192.168.10.28/com.xianzhi.apis.xiamendeposit.XiamenDepositApi?application=xianzhi_admin_consumer&category=providers,configurators,routers&check=false&dubbo=2.5.3&interface=com.xianzhi.apis.xiamendeposit.XiamenDepositApi&methods=queryTransaction,fundTransferredOut,cgtQueryTransBatch,queryDepositProductInfor,queryDepositUserInfor,modifyCgtProductStatus,queryPlatformInfor,syncTransaction&pid=25436&revision=14.13&side=consumer&timestamp=1574857342488
    private volatile URL consumerUrl;
    /**
     * {@link AbstractDirectory#setRouters}
     */
    private volatile List<Router> routers;

    public AbstractDirectory(URL url) {
        this(url, null);
    }

    public AbstractDirectory(URL url, List<Router> routers) {
        this(url, url, routers);
    }

    public AbstractDirectory(URL url, URL consumerUrl, List<Router> routers) {
        if (url == null)
            throw new IllegalArgumentException("url == null");
        this.url = url;
        this.consumerUrl = consumerUrl;
        setRouters(routers);
    }

    @Override
    public List<Invoker<T>> list(Invocation invocation) throws RpcException {
        if (destroyed) {
            throw new RpcException("Directory already destroyed .url: " + getUrl());
        }
        List<Invoker<T>> invokers = doList(invocation);
        List<Router> localRouters = this.routers; // local reference
        if (localRouters != null && !localRouters.isEmpty()) {
            for (Router router : localRouters) {
                try {
                    if (router.getUrl() == null || router.getUrl().getParameter(Constants.RUNTIME_KEY, false)) {
                        invokers = router.route(invokers, getConsumerUrl(), invocation);
                    }
                } catch (Throwable t) {
                    logger.error("Failed to execute router: " + getUrl() + ", cause: " + t.getMessage(), t);
                }
            }
        }
        return invokers;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    public List<Router> getRouters() {
        return routers;
    }

    /**
     *
     * @param routers null
     */
    protected void setRouters(List<Router> routers) {
        // copy list
        routers = routers == null ? new ArrayList<Router>() : new ArrayList<Router>(routers);
        // append url router  debug 得出 ：routerkey = null
        String routerkey = url.getParameter(Constants.ROUTER_KEY);
        if (routerkey != null && routerkey.length() > 0) {
            RouterFactory routerFactory = ExtensionLoader.getExtensionLoader(RouterFactory.class).getExtension(routerkey);
            routers.add(routerFactory.getRouter(url));
        }
        // append mock invoker selector
        routers.add(new MockInvokersSelector());
        routers.add(new TagRouter());
        Collections.sort(routers);
        this.routers = routers;
    }

    public URL getConsumerUrl() {
        return consumerUrl;
    }

    public void setConsumerUrl(URL consumerUrl) {
        this.consumerUrl = consumerUrl;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void destroy() {
        destroyed = true;
    }

    protected abstract List<Invoker<T>> doList(Invocation invocation) throws RpcException;

}
