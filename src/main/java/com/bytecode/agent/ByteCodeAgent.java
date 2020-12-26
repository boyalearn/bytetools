package com.bytecode.agent;

import com.bytecode.config.AgentType;
import com.bytecode.config.ConfigUtils;
import com.bytecode.method.MethodUtil;
import com.bytecode.utils.StringUtils;
import javassist.CtClass;
import javassist.CtMethod;

public class ByteCodeAgent implements TransformerAgent {
    @Override
    public CtClass transform(String className, ClassLoader loader) throws Exception {
        CtClass ctClass = MethodUtil.getCtClass(loader, className);

        CtMethod[] methods = ctClass.getDeclaredMethods();
        for (CtMethod method : methods) {
            if (MethodUtil.shouldSkipCommonMethod(method.getName())) {
                continue;
            }
            String packageName = StringUtils.getPackageName(className);
            String clazzName = StringUtils.getClassName(className);
            if (AgentType.TIME.equals(ConfigUtils.getAgentType())
                    && ConfigUtils.shouldIncludeClass(packageName, clazzName, method.getName()))
                MethodUtil.changeMethodToMonitorSpendTime(ctClass, method);
            if (AgentType.EXCEPTION.equals(ConfigUtils.getAgentType())
                    && ConfigUtils.shouldIncludeClass(packageName, clazzName, method.getName()))
                MethodUtil.changeMethodToMonitorException(ctClass, method);

        }
        return ctClass;
    }
}
