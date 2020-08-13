package com.bytecode.transformer;

import com.bytecode.agent.ByteCodeAgent;
import com.bytecode.agent.CglibAopAgent;
import com.bytecode.agent.TransformerAgent;
import com.bytecode.config.ConfigUtils;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.Modifier;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class CodeByteTransformer implements ClassFileTransformer {

    private TransformerAgent agent = new ByteCodeAgent();

    private TransformerAgent cglibAgent = new CglibAopAgent();

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (null == className) {
            return null;
        }
        CtClass tmpCtClass;
        CtClass ctClass;
        try {
            if (className.contains(CglibAopAgent.CLASS_NAME)) {
                ClassPool pool = ClassPool.getDefault();
                pool.appendClassPath(new LoaderClassPath(loader));
                ctClass = pool.getCtClass(className.replace("/", "."));

                tmpCtClass = cglibAgent.transform(ctClass, className, loader);
                return tmpCtClass.toBytecode();

            } else {

                if (!ConfigUtils.shouldIncludeClassName(className)) {
                    return null;
                }

                ClassPool pool = ClassPool.getDefault();
                pool.appendClassPath(new LoaderClassPath(loader));
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
                }
            }
        } catch (Exception e) {
            if (className.contains("$$EnhancerBySpringCGLIB$$")) {
                // no thing to do
            } else if (className.contains("$$FastClassBySpringCGLIB$$")) {
                ConfigUtils.addCglibClass(className);
            } else {
                e.printStackTrace();
            }
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
