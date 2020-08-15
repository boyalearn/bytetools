package com.bytecode.adapter;

import com.bytecode.agent.ByteCodeAgent;
import com.bytecode.agent.TransformerAgent;
import com.bytecode.config.ConfigUtils;
import com.bytecode.method.MethodUtil;
import javassist.CtClass;
import javassist.Modifier;

public class CommonAdapter implements AgentAdapter {

    private TransformerAgent agent = new ByteCodeAgent();

    @Override
    public TransformerAgent adapter(String className, ClassLoader loader) throws Exception {
        if (!ConfigUtils.shouldIncludeClassName(className)) {
            return null;
        }
        CtClass ctClass = MethodUtil.getCtClass(loader, className);

        if (shouldSkipCommonClass(ctClass)) {
            return null;
        }
        return agent;
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
