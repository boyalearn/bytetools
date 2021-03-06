package com.bytecode.agent;

import com.bytecode.method.MethodUtil;
import javassist.*;

public class MybatisAgent implements TransformerAgent {
    public static final String CLASS_NAME = "CachingExecutor";

    private final String QUERY_METHOD_NAME = "query";

    private final String UPDATE_METHOD_NAME = "update";

    @Override
    public CtClass transform(String className, ClassLoader loader) throws Exception {
        CtClass ctClass = MethodUtil.getCtClass(loader, className);
        CtMethod[] methods = ctClass.getDeclaredMethods();
        for (CtMethod method : methods) {
            if (isQueryMethod(method) || isUpdateMethod(method)) {
                changeMethodToMonitorSQLTime(ctClass, method);
            }
        }
        return ctClass;
    }

    private boolean isQueryMethod(CtMethod method) throws NotFoundException {
        int length = method.getParameterTypes().length;
        if (6 == length && QUERY_METHOD_NAME.equals(method.getName())) {
            return true;
        }
        return false;
    }

    private boolean isUpdateMethod(CtMethod method) throws NotFoundException {
        if (UPDATE_METHOD_NAME.equals(method.getName())) {
            return true;
        }
        return false;
    }

    public void changeMethodToMonitorSQLTime(CtClass ctClass, CtMethod ctMethod) throws CannotCompileException, NotFoundException {
        String methodName = ctMethod.getName();
        String copyMethodName = "_doSQLMonitor" + methodName;
        CtMethod newMethod = CtNewMethod.copy(ctMethod, methodName, ctClass, null);
        newMethod.setName(copyMethodName);
        ctClass.addMethod(newMethod);
        //构造原始方法的方法体
        ctMethod.setBody(buildMonitorTimeAopMethodBody(copyMethodName));
    }

    private String buildMonitorTimeAopMethodBody(String copyMethodName) throws NotFoundException, CannotCompileException {
        StringBuilder body = new StringBuilder("");
        body.append("{");
        body.append("    long __startTime=System.currentTimeMillis();");
        body.append("    try {");
        body.append("        return  "+copyMethodName+"($$);");
        body.append("    } catch( Exception e) {");
        body.append("        throw e;");
        body.append("    }finally{ ");
        body.append("        String _sql = $1.getSqlSource().getBoundSql($2).getSql();");
        body.append("        com.bytecode.utils.MonitorUtil.monitor( _sql, System.currentTimeMillis()-__startTime );");
        body.append("    }");
        body.append("}");
        return body.toString();
    }
}
