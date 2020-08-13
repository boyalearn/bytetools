# bytetools


## 特性

#### 监听指定方法的耗时时间

#### 打印方法上的异常


可以打印异常的方法堆栈

` void loadConf() throws IOException`


## 使用方式

#### 配置文件

```xml
<log-file fileName="D://monitor.log" />
<agent type="com.bytecode.config.AgentType.TIME">
    <include package="com.test" clazz="" method=""/>
    <exclude package="com.test.service" clazz="" method="eat"/>
</agent>
<agent type="com.bytecode.config.AgentType.EXCEPTION">
    <include package="com.test.service" clazz="" method="eat"/>
    <exclude package="com.test" clazz="" method="end"/>
</agent>
```

#### 运行方式

在java启动命令上添加 -javaagent:xxxx.jar=configPathFile




#### bug修复方式

JDK8以上正常运行

<!-- https://mvnrepository.com/artifact/org.javassist/javassist -->
<dependency>
    <groupId>org.javassist</groupId>
    <artifactId>javassist</artifactId>
    <version>3.20.0-GA</version>
</dependency>


package com.bytecode.transformer;

import com.bytecode.agent.ByteCodeAgent;
import com.bytecode.agent.CglibAopAgent;
import com.bytecode.agent.TransformerAgent;

import com.bytecode.config.ConfigUtils;
import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;

import java.security.ProtectionDomain;

public class CodeByteTransformer implements ClassFileTransformer {

    private TransformerAgent agent=new ByteCodeAgent();

    private TransformerAgent cglibAgent=new CglibAopAgent();

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
        ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if(null==className){
            return null;
        }
        try {

            CtClass tmpCtClass;
            CtClass ctClass;
            if (null!=className&&className.contains("DynamicAdvisedInterceptor")) {
                ClassPool pool = ClassPool.getDefault();
                pool.appendClassPath(new LoaderClassPath(loader));
                try {
                    ctClass = pool.getCtClass(className.replace("/", "."));
                } catch (NotFoundException e) {
                    e.printStackTrace();
                    return null;
                }

                tmpCtClass = cglibAgent.transform(ctClass, className, loader);
                try {
                    return tmpCtClass.toBytecode();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {

                if (!ConfigUtils.shouldIncludeClassName(className)) {
                    return null;
                }
                ClassPool pool = ClassPool.getDefault();
                pool.appendClassPath(new LoaderClassPath(loader));

                try {
                    ctClass = pool.getCtClass(className.replace("/", "."));

                    if (shouldSkipCommonClass(ctClass)) {
                        return null;
                    }
                    tmpCtClass = agent.transform(ctClass, className, loader);

                    if (null != tmpCtClass) {
                        try {
                            return tmpCtClass.toBytecode();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.err.println("modify default class Name:" + className);

                    }
                } catch (NotFoundException e) {
                    if (className.contains("$$EnhancerBySpringCGLIB$$")) {
                        // no thing to do
                    } else if (className.contains("$$FastClassBySpringCGLIB$$")) {
                        ConfigUtils.addCglibClass(className);
                    } else {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private boolean shouldSkipCommonClass(CtClass ctClass) {
        if (ctClass.isFrozen() || Modifier.isInterface(ctClass.getModifiers()) ||
                Modifier.isAnnotation(ctClass.getModifiers()) ||
                Modifier.isEnum(ctClass.getModifiers())) {
            return true;
        }
        return false;
    }

}

