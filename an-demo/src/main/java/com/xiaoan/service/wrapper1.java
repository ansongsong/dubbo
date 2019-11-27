package com.xiaoan.service;

/**
 * @Author: ansongsong
 * @Date: 2019/11/26
 */
//package com.alibaba.dubbo.common.bytecode;

import com.alibaba.dubbo.common.bytecode.ClassGenerator;
import com.alibaba.dubbo.common.bytecode.Wrapper;
import com.alibaba.dubbo.demo.provider.DemoDAO;
import com.alibaba.dubbo.demo.provider.DemoServiceImpl;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 1. 该类通过 dubbo-common 模块的 bytecode 模块的 Wrapper 类，自动生成，使用 Javassist 技术
 * 2. 不同于生成的 proxy类，不实现 Service 接口类，而是在 #invokeMethod(paramObject, paramString, paramArrayOfClass, paramArrayOfObject) 方法，提供给 Invoker#invoke(invocation) 中调用，
 *      统一分发请求到 Service 对应的方法。从职能上来看，有一点像硬编码的 Controller 。
 * 3. 一个生成的 Wrapper类，只对应一个 Service ，从第 73 行的代码，我们也可以看出。
 */
public class wrapper1 extends Wrapper implements ClassGenerator.DC {
    public static String[] pns;
    public static Map pts;
    public static String[] mns;
    public static String[] dmns;
    public static Class[] mts0;
    public static Class[] mts1;
    public static Class[] mts2;

    public String[] getPropertyNames() {
        return pns;
    }

    public boolean hasProperty(String paramString) {
        return pts.containsKey(paramString);
    }

    public Class getPropertyType(String paramString) {
        return (Class) pts.get(paramString);
    }

    public String[] getMethodNames() {
        return mns;
    }

    public String[] getDeclaredMethodNames() {
        return dmns;
    }

    public void setPropertyValue(Object paramObject1, String paramString, Object paramObject2) {
        DemoServiceImpl w;
        try {
            w = (DemoServiceImpl) paramObject1;
        } catch (Throwable localThrowable) {
            throw new IllegalArgumentException(localThrowable);
        }
        if (paramString.equals("test01")) {
            w.test01 = ((String) paramObject2);
            return;
        }
        if (paramString.equals("demoDAO")) {
            localDemoServiceImpl.setDemoDAO((DemoDAO) paramObject2);
            return;
        }
        throw new NoSuchPropertyException("Not found property \"" + paramString + "\" filed or setter method in class com.alibaba.dubbo.demo.provider.DemoServiceImpl.");
    }

    public Object getPropertyValue(Object paramObject, String paramString) {
        DemoServiceImpl w;
        try {
            w = (DemoServiceImpl) paramObject;
        } catch (Throwable localThrowable) {
            throw new IllegalArgumentException(localThrowable);
        }
        if (paramString.equals("test01")) {
            return localDemoServiceImpl.test01;
        }
        throw new NoSuchPropertyException("Not found property \"" + paramString + "\" filed or setter method in class com.alibaba.dubbo.demo.provider.DemoServiceImpl.");
    }

    public Object invokeMethod(Object paramObject, String paramString, Class[] paramArrayOfClass, Object[] paramArrayOfObject)
            throws InvocationTargetException {
        DemoServiceImpl w;
        try {
            w = (DemoServiceImpl) paramObject;
        } catch (Throwable localThrowable1) {
            throw new IllegalArgumentException(localThrowable1);
        }
        try {
            if ("sayHello".equals(paramString) && paramArrayOfClass.length == 1) {
                return w.sayHello((String) paramArrayOfObject[0]);
            }
            if ("bye".equals(paramString) && paramArrayOfClass.length == 1) {
                w.bye((Object) paramArrayOfObject[0]);
                return null;
            }
            if ("setDemoDAO".equals(paramString) && paramArrayOfClass.length == 1) {
                w.setDemoDAO((DemoDAO) paramArrayOfObject[0]);
                return null;
            }
        } catch (Throwable localThrowable2) {
            throw new InvocationTargetException(localThrowable2);
        }
        throw new NoSuchMethodException("Not found method \"" + paramString + "\" in class com.alibaba.dubbo.demo.provider.DemoServiceImpl.");
    }
}
