package com.bytecode.agent;

import javassist.*;

public class CglibAopAgent implements TransformerAgent {

    public static final String CLASS_NAME = "DynamicAdvisedInterceptor";

    @Override
    public CtClass transform(CtClass ctClass, String className, ClassLoader loader) throws Exception {
        CtMethod[] methods = ctClass.getDeclaredMethods();
        for (CtMethod method : methods) {
            if (method.getName().contains("intercept")) {
                changeMethodToMonitorSpendTime(ctClass, method);
            }
        }
        return ctClass;
    }

    public void changeMethodToMonitorSpendTime(CtClass ctClass, CtMethod ctMethod) throws CannotCompileException, NotFoundException {
        String methodName = ctMethod.getName();
        String copyMethodName = "_doAgent" + methodName;
        CtMethod newMethod = CtNewMethod.copy(ctMethod, methodName, ctClass, null);
        newMethod.setName(copyMethodName);
        ctClass.addMethod(newMethod);
        //构造原始方法的方法体
        ctMethod.setBody(buildMonitorTimeAopMethodBody(ctMethod, copyMethodName));
    }

    private String buildMonitorTimeAopMethodBody(CtMethod ctMethod, String copyMethodName) throws NotFoundException, CannotCompileException {
        StringBuilder methodBody = new StringBuilder();
        methodBody.append("{");
        methodBody.append("    long __startTime=System.currentTimeMillis();");
        methodBody.append("    try {");
        methodBody.append("        return  #copyMethodName#($$);");
        methodBody.append("    } catch( Exception e) {");
        methodBody.append("        throw e;");
        methodBody.append("    }finally{ ");
        methodBody.append("        String _className = $1.getClass().getName();");
        methodBody.append("        String _methodName = ((java.lang.reflect.Method)$2).getName();");
        methodBody.append("        String _parameters = com.bytecode.method.MethodUtil.getMethodParameter( $2 );");
        methodBody.append("        if( com.bytecode.config.ConfigUtils.shouldInclude( _className, _methodName ) )");
        methodBody.append("            com.bytecode.utils.MonitorUtil.monitor( _className+\".\"+_methodName+\"(\"+_parameters+\")\", System.currentTimeMillis()-__startTime );");
        methodBody.append("    }");
        methodBody.append("}");
        return methodBody.toString().replaceAll("#copyMethodName#", copyMethodName);
    }

}
