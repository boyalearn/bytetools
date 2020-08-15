package com.bytecode.transformer;

import com.bytecode.adapter.AgentAdapter;
import com.bytecode.adapter.CommonAdapter;
import com.bytecode.adapter.SqlAndAopAdapter;
import com.bytecode.agent.TransformerAgent;
import com.bytecode.config.ConfigUtils;
import javassist.CtClass;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class CodeByteTransformer implements ClassFileTransformer {

    private AgentAdapter mybatisAopAgent = new SqlAndAopAdapter();

    private AgentAdapter commonAdapter = new CommonAdapter();

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (null == className) {
            return null;
        }
        try {
            CtClass tmpCtClass;
            TransformerAgent agent = mybatisAopAgent.adapter(className, loader);
            if (null != agent) {
                tmpCtClass = agent.transform(className, loader);
                if (null != tmpCtClass) {
                    return tmpCtClass.toBytecode();
                }
            }
            agent = commonAdapter.adapter(className, loader);
            if (null != agent) {
                tmpCtClass = agent.transform(className, loader);
                if (null != tmpCtClass) {
                    return tmpCtClass.toBytecode();
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


}
