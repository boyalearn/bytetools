package com.bytecode.transformer;

import com.bytecode.agent.ByteCodeAgent;
import com.bytecode.agent.CglibAopAgent;
import com.bytecode.agent.MybatisAgent;
import com.bytecode.agent.TransformerAgent;
import com.bytecode.config.AgentType;
import com.bytecode.config.ConfigUtils;
import javassist.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class CodeByteTransformer implements ClassFileTransformer {

    private TransformerAgent agent = new ByteCodeAgent();

    private TransformerAgent cglibAgent = new CglibAopAgent();

    private TransformerAgent mybatisAgent = new MybatisAgent();

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (null == className) {
            return null;
        }
        try {
            CtClass tmpCtClass;
            CtClass ctClass;

            if (AgentType.TIME.equals(ConfigUtils.getAgentType()) && className.contains(CglibAopAgent.CLASS_NAME)) {
                tmpCtClass = cglibAgent.transform(getCtClass(loader, className), className, loader);
                return tmpCtClass.toBytecode();
            } else if (AgentType.TIME.equals(ConfigUtils.getAgentType()) && className.contains(MybatisAgent.CLASS_NAME)) {
                tmpCtClass = mybatisAgent.transform(getCtClass(loader, className), className, loader);
                return tmpCtClass.toBytecode();
            } else {
                if (!ConfigUtils.shouldIncludeClassName(className)) {
                    return null;
                }

                ctClass = getCtClass(loader, className);

                if (shouldSkipCommonClass(ctClass)) {
                    return null;
                }
                tmpCtClass = agent.transform(ctClass, className, loader);
                return tmpCtClass.toBytecode();
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

    private CtClass getCtClass(ClassLoader loader, String className) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        pool.appendClassPath(new LoaderClassPath(loader));
        return pool.getCtClass(className.replace("/", "."));
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
