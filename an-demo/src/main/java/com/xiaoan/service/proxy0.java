package com.xiaoan.service;

/**
 * @Author: ansongsong
 * @Date: 2019/11/26
 */
//package com.alibaba.dubbo.common.bytecode;

import com.alibaba.dubbo.common.bytecode.ClassGenerator;
import com.alibaba.dubbo.demo.DemoService;
import com.alibaba.dubbo.rpc.service.EchoService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 该类通过 dubbo-common 模块的 bytecode 模块的 Proxy 类，自动生成，使用 Javassist 技术。
 * 生成的 proxy 类会实现我们定义的 Service 接口( 例如，此处是 DemoService )。
 * #bye(Object) 和 #sayHello(Object) 方法，是我们定义在 DemoService 的接口方法，在生成的 proxy 类中，实现这些定义在接口中的方法，收拢统一调用
 * java.lang.reflect.InvocationHandler#invoke(proxy, method, args) 方法。通过这样的方式，可以调用到最终的 Invoker#invoke(Invocation) 方法，实现 RPC 调用
 * 注意，此处我们一直用的 proxy 一直是小写的，这是为什么呢？请见下文大写的 Proxy 类。
 */
public class proxy0 implements ClassGenerator.DC, EchoService, DemoService{

        public static Method[] methods;
        private InvocationHandler handler;

        public void bye(Object paramObject) {
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = paramObject;
            Object localObject = this.handler.invoke(this, methods[0], arrayOfObject);
        }

        public String sayHello(String paramString) {
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = paramString;
            Object localObject = this.handler.invoke(this, methods[1], arrayOfObject);
            return (String) localObject;
        }

        public Object $echo(Object paramObject) {
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = paramObject;
            Object localObject = this.handler.invoke(this, methods[2], arrayOfObject);
            return (Object) localObject;
        }

        public proxy0() {
        }

        public proxy0(InvocationHandler paramInvocationHandler) {
            this.handler = paramInvocationHandler;
        }
}
