package com.bytecode.agent;

import com.bytecode.config.ConfigUtils;
import com.bytecode.method.MethodUtil;
import com.bytecode.utils.StringUtils;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.reflect.Method;

public class ByteCodeAgent implements TransformerAgent {
    @Override
    public CtClass transform(CtClass ctClass, String className, ClassLoader loader) {
        CtMethod[] methods = ctClass.getDeclaredMethods();
        for (CtMethod method : methods) {
            if(MethodUtil.shouldSkipCommonMethod(method.getName())){
                continue;
            }
            String packageName= StringUtils.getPackageName(className);
            String clazzName=StringUtils.getClassName(className);
            try {
                if (ConfigUtils.shouldAppendSpendTimeCode(packageName, clazzName, method.getName()))
                    MethodUtil.changeMethodToMonitorSpendTime(ctClass, method);
                if (ConfigUtils.shouldAppendExceptionCode(packageName, clazzName, method.getName()))
                    MethodUtil.changeMethodToMonitorException(ctClass, method);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return ctClass;
    }
}
