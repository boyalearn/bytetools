package com.bytecode.transformer;

import com.bytecode.config.ConfigUtils;
import com.bytecode.filter.AgentFilterChain;
import javassist.CtClass;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class CodeByteTransformer implements ClassFileTransformer {

    private AgentFilterChain agentFilterChain = new AgentFilterChain();

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (null == className) {
            return null;
        }
        try {
            CtClass tmpCtClass = agentFilterChain.doTransform(className, loader);
            if (null != tmpCtClass) {
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


}
