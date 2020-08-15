package com.bytecode.method;

import com.bytecode.log.Log;
import javassist.*;

import java.lang.reflect.Method;

public class MethodUtil {
    public static boolean shouldSkipCommonMethod(String methodName) {
        if ("main".equals(methodName)) {
            return true;
        }
        if ("toString".equals(methodName)) {
            return true;
        }
        if ("equals".equals(methodName)) {
            return true;
        }
        if ("hashCode".equals(methodName)) {
            return true;
        }
        return false;
    }

    public static void changeMethodToMonitorSpendTime(CtClass ctClass, CtMethod ctMethod) throws CannotCompileException, NotFoundException {
        String changedMethodName = ctMethod.getName();
        String copyMethodName = "_do" + changedMethodName;
        CtMethod copyMethod = CtNewMethod.copy(ctMethod, changedMethodName, ctClass, null);
        copyMethod.setName(copyMethodName);
        ctClass.addMethod(copyMethod);
        //构造原始方法的方法体
        ctMethod.setBody(buildMonitorTimeMethodBody(ctMethod, copyMethodName));
    }

    private static String buildMonitorTimeMethodBody(CtMethod ctMethod, String copyMethodName) throws NotFoundException, CannotCompileException {
        StringBuilder body = new StringBuilder();

        body.append("{");
        body.append("    long _startTime = System.currentTimeMillis();");
        body.append("    try{");
        body.append("        #return# #copyMethodName#($$);");
        body.append("    }finally{");
        body.append("        com.bytecode.utils.MonitorUtil.monitor( \"#methodName#\" , System.currentTimeMillis()-_startTime );");
        body.append("    }");
        body.append("}");

        String methodName = ctMethod.getLongName();
        String methodBody = body.toString().replaceAll("#copyMethodName#", copyMethodName)
                .replaceAll("#methodName#", methodName);

        if ("void".equals(ctMethod.getReturnType().getName())) {
            return methodBody.replaceAll("#return#", "");
        } else {
            return methodBody.replaceAll("#return#", "return");
        }
    }


    public static void changeMethodToMonitorException(CtClass ctClass, CtMethod method) {
        try {
            changeMethodExceptionInArgs(method);
            changeMethodExceptionMethodSignature(ctClass, method);
        } catch (Exception e) {
            Log.log(e);
        }
    }

    private static void changeMethodExceptionInArgs(CtMethod method) throws NotFoundException, CannotCompileException {
        CtClass[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (isExceptionClass(parameterTypes[i])) {
                method.insertBefore("{com.bytecode.utils.MonitorUtil.printE($" + (i + 1) + ");}");
            }
        }
    }

    private static boolean isExceptionClass(CtClass paramType) throws NotFoundException {
        if (paramType.getSimpleName().equals("Exception") || paramType.getSimpleName().equals("Throwable")) {
            return true;
        }
        if (null != paramType.getSuperclass()) {
            if (isExceptionClass(paramType.getSuperclass())) {
                return true;
            }
        }
        return false;
    }

    private static void changeMethodExceptionMethodSignature(CtClass ctClass, CtMethod method) throws NotFoundException, CannotCompileException {
        if (method.getExceptionTypes().length > 0) {
            String methodName = method.getName();
            String newName = "_E" + methodName;
            CtMethod newMethod = CtNewMethod.copy(method, methodName, ctClass, null);
            newMethod.setName(newName);
            ctClass.addMethod(newMethod);
            method.setBody(buildMonitorExceptionMethodBody(method, newName));
        }

    }

    private static String buildMonitorExceptionMethodBody(CtMethod method, String newName) throws NotFoundException {
        StringBuilder body = new StringBuilder();
        body.append("{");
        body.append("    try{");
        body.append("        #return# #newMethod#($$); ");
        body.append("    }catch(Exception e){");
        body.append("        com.bytecode.utils.MonitorUtil.printE(e);");
        body.append("        throw e;");
        body.append("    }");
        body.append("}");

        String methodBody = body.toString().replaceAll("#newMethod#", newName);
        if ("void".equals(method.getReturnType().getName())) {
            return methodBody.replaceAll("#return#", "");
        } else {
            return methodBody.replaceAll("#return#", "return");
        }
    }

    public static String getMethodParameter(Method method) {
        StringBuffer stringBuffer = new StringBuffer();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 0) {
            return stringBuffer.toString();
        }
        for (Class<?> parameterType : parameterTypes) {
            stringBuffer.append("," + parameterType.getTypeName());
        }
        return stringBuffer.toString().substring(1);
    }
}
