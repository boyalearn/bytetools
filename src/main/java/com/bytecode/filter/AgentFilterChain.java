package com.bytecode.filter;

import com.bytecode.agent.ByteCodeAgent;
import com.bytecode.agent.CglibAopAgent;
import com.bytecode.agent.MybatisAgent;
import com.bytecode.config.AgentType;
import com.bytecode.config.ConfigUtils;
import com.bytecode.method.MethodUtil;
import javassist.CtClass;
import javassist.Modifier;

public class AgentFilterChain {

    private MybatisAgent mybatisAgent = new MybatisAgent();

    private ByteCodeAgent byteCodeAgent = new ByteCodeAgent();

    private CglibAopAgent cglibAopAgent = new CglibAopAgent();

    public CtClass doTransform(String className, ClassLoader loader) throws Exception {

        if (!ConfigUtils.shouldIncludeClassName(className)) {
            return null;
        }
        CtClass ctClass = MethodUtil.getCtClass(loader, className);

        if (shouldSkipCommonClass(ctClass)) {
            return null;
        }

        CtClass modifyClass = null;
        if (AgentType.TIME.equals(ConfigUtils.getAgentType()) && className.contains(MybatisAgent.CLASS_NAME)) {
            modifyClass = mybatisAgent.transform(className, loader);
        }
        if (null != modifyClass) {
            return modifyClass;
        }

        if (AgentType.TIME.equals(ConfigUtils.getAgentType()) && className.contains(CglibAopAgent.CLASS_NAME)) {
            modifyClass = cglibAopAgent.transform(className, loader);
        }
        if (null != modifyClass) {
            return modifyClass;
        }

        modifyClass = byteCodeAgent.transform(className, loader);
        if (null != modifyClass) {
            return modifyClass;
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
