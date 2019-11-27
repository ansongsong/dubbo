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
package com.alibaba.dubbo.common.compiler.support;

import com.alibaba.dubbo.common.utils.ClassHelper;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 动态编译
 * Javassist 是一个开源的分析、编辑和创建 Java 字节码的类库。通过使用Javassist 对字节码操作可以实现动态 ”AOP” 框架。
 * 关于 Java 字节码的处理，目前有很多工具，如 bcel，asm( cglib只是对asm又封装了一层 )。不过这些都需要直接跟虚拟机指令打交道。
 * Javassist 的主要的优点，在于简单，而且快速，直接使用 Java 编码的形式，而不需要了解虚拟机指令，就能动态改变类的结构，或者动态生成类。
 * JavassistCompiler. (SPI, Singleton, ThreadSafe)
 */
public class JavassistCompiler extends AbstractCompiler {
    /**
     * 正则 - 匹配 import
     */
    private static final Pattern IMPORT_PATTERN = Pattern.compile("import\\s+([\\w\\.\\*]+);\n");
    /**
     * 正则 - 匹配 extends
     */
    private static final Pattern EXTENDS_PATTERN = Pattern.compile("\\s+extends\\s+([\\w\\.]+)[^\\{]*\\{\n");
    /**
     * 正则 - 匹配 implements
     */
    private static final Pattern IMPLEMENTS_PATTERN = Pattern.compile("\\s+implements\\s+([\\w\\.]+)\\s*\\{\n");
    /**
     * 正则 - 匹配方法
     */
    private static final Pattern METHODS_PATTERN = Pattern.compile("\n(private|public|protected)\\s+");
    /**
     * 正则 - 匹配变量
     */
    private static final Pattern FIELD_PATTERN = Pattern.compile("[^\n]+=[^\n]+;");

    /**
     * 因为传入的是 Java 源代码 source ，需要通过正则匹配出 import、extends、implements、方法、变量，传递给 Javassist API ，进行类生成。
     * 🙂 如果胖友对 Javassist 的 API 不是很了解，可以看完整体逻辑，回看下上面提供的文档。挺有趣的
     * @param name 类名
     * @param source 代码
     * @return
     * @throws Throwable
     */
    @Override
    public Class<?> doCompile(String name, String source) throws Throwable {
        // 获得类名
        int i = name.lastIndexOf('.');
        String className = i < 0 ? name : name.substring(i + 1);
        // 创建 ClassPool 对象   创建 ClassPool 对象。ClassPool 是一个 CtClass 对象的 hash 表，类名做为 key 。ClassPool 的 #get(key) 搜索 hash 表找到与指定 key 关联的 CtClass 对象。如果没有找到 CtClass 对象，#get(key) 读一个类文件构建新的 CtClass 对象，它是被记录在 hash 表中然后返回这个对象。
        ClassPool pool = new ClassPool(true);
        // 设置类搜索路径
        pool.appendClassPath(new LoaderClassPath(ClassHelper.getCallerClassLoader(getClass())));
        // 匹配 import
        Matcher matcher = IMPORT_PATTERN.matcher(source);
        // 引用的包名
        List<String> importPackages = new ArrayList<String>();
        // 引用的类名
        Map<String, String> fullNames = new HashMap<String, String>();
        while (matcher.find()) {
            String pkg = matcher.group(1);
            if (pkg.endsWith(".*")) {// 引用整个包下的类/接口
                String pkgName = pkg.substring(0, pkg.length() - 2);
                // 引用包
                pool.importPackage(pkgName);
                importPackages.add(pkgName);
            } else {// 引用指定类/接口
                int pi = pkg.lastIndexOf('.');
                if (pi > 0) {
                    String pkgName = pkg.substring(0, pi);
                    pool.importPackage(pkgName);
                    importPackages.add(pkgName);
                    fullNames.put(pkg.substring(pi + 1), pkg);
                }
            }
        }
        String[] packages = importPackages.toArray(new String[0]);
        // 匹配 extends
        matcher = EXTENDS_PATTERN.matcher(source);
        CtClass cls;
        if (matcher.find()) {
            String extend = matcher.group(1).trim();
            String extendClass;
            if (extend.contains(".")) {// 内嵌的类，例如：extends A.B
                extendClass = extend;
            } else if (fullNames.containsKey(extend)) {// 指定引用的类
                extendClass = fullNames.get(extend);
            } else { // 引用整个包下的类
                extendClass = ClassUtils.forName(packages, extend).getName();
            }
            // 创建 CtClass 对象 --- 创建带继承的类
            cls = pool.makeClass(name, pool.get(extendClass));
        } else {
            // 创建 CtClass 对象 --- 创建类
            cls = pool.makeClass(name);
        }
        // 匹配 implements
        matcher = IMPLEMENTS_PATTERN.matcher(source);
        if (matcher.find()) {
            String[] ifaces = matcher.group(1).trim().split("\\,");
            for (String iface : ifaces) {
                iface = iface.trim();
                String ifaceClass;
                if (iface.contains(".")) {// 内嵌的接口，例如：extends A.B
                    ifaceClass = iface;
                } else if (fullNames.containsKey(iface)) {// 指定引用的接口
                    ifaceClass = fullNames.get(iface);
                } else {// 引用整个包下的接口
                    ifaceClass = ClassUtils.forName(packages, iface).getName();
                }
                // 添加类的接口
                cls.addInterface(pool.get(ifaceClass));
            }
        }
        // 获得类中的内容，即首末 {} 的内容。
        String body = source.substring(source.indexOf("{") + 1, source.length() - 1);
        // 匹配 method 。使用分隔的方式，实际上，分隔出来的不仅仅有方法。
        String[] methods = METHODS_PATTERN.split(body);
        for (String method : methods) {
            method = method.trim();
            if (method.length() > 0) {
                if (method.startsWith(className)) {// 构造方法
//                    添加类的构造方法
                    cls.addConstructor(CtNewConstructor.make("public " + method, cls));
                } else if (FIELD_PATTERN.matcher(method).matches()) {
//                    添加类的属性
                    cls.addField(CtField.make("private " + method, cls));
                } else {
                    // 添加类的方法
                    cls.addMethod(CtNewMethod.make("public " + method, cls));
                }
            }
        }
        // 生成类
        // JavassistCompiler.class.getProtectionDomain() =》 设置保护域和 JavassistCompiler 一致，即 `#getClass()` 方法。
        // 深入见 《Java安全——安全管理器、访问控制器和类装载器》https://www.zybuluo.com/changedi/note/417132
        return cls.toClass(ClassHelper.getCallerClassLoader(getClass()), JavassistCompiler.class.getProtectionDomain());
    }

}
