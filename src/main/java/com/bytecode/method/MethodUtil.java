package com.bytecode.method;

import com.bytecode.utils.MonitorPrinter;
import javassist.*;

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
        String oldName = ctMethod.getName();
        String newName = oldName + "__doTimeMethod";
        CtMethod newMethod = CtNewMethod.copy(ctMethod, oldName, ctClass, null);
        newMethod.setName(newName);
        ctClass.addMethod(newMethod);
        //构造原始方法的方法体
        ctMethod.setBody(buildMonitorTimeMethodBody(ctClass, ctMethod, oldName, newName));
    }

    private static String buildMonitorTimeMethodBody(CtClass ctClass, CtMethod ctMethod, String oldName, String newName) throws NotFoundException {
        StringBuilder body = new StringBuilder();
        body.append("{");
        body.append("  long start = System.currentTimeMillis();");
        String type = ctMethod.getReturnType().getName();
        if (!"void".equals(type)) {
        body.append("  "+type + " result = "+newName + "($$);");
        }
        body.append("  "+newName + "($$);");
        body.append("  com.bytecode.utils.MonitorPrinter.println(\"Call method " + ctMethod.getLongName() + " took \" + (System.currentTimeMillis()-start) + \" ms.\");");
        if (!"void".equals(type)) {
        body.append("  return result;");
        }
        body.append("}");
        return body.toString();
    }

    public static void changeMethodToMonitorException(CtClass ctClass, CtMethod method) {
        try {
            changeMethodExceptionInArgs(method);
        } catch (Exception e) {
            MonitorPrinter.printException(e);
        }
        try {
            changeMethodExceptionMethodSignature(ctClass, method);
        } catch (Exception e) {
            MonitorPrinter.printException(e);
        }
    }

    private static void changeMethodExceptionInArgs(CtMethod method) throws NotFoundException, CannotCompileException {
        CtClass[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (isExceptionClass(parameterTypes[i])) {
                method.insertBefore("{com.bytecode.utils.MonitorPrinter.printMonitorException($" + (i + 1) + ");}");
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
            String oldName = method.getName();
            String newName = oldName + "_exception";
            CtMethod newMethod = CtNewMethod.copy(method, oldName, ctClass, null);
            newMethod.setName(newName);
            ctClass.addMethod(newMethod);
            method.setBody(buildMonitorExceptionMethodBody(newName));
        }

    }
    private static String buildMonitorExceptionMethodBody(String newName) throws NotFoundException {
        StringBuilder body = new StringBuilder();
        body.append("{");
        body.append("  try{");
        body.append("    return " + newName + "($$); ");
        body.append("  }catch(Exception e){");
        body.append("    com.bytecode.utils.MonitorPrinter.printMonitorException(e);");
        body.append("    throw e;");
        body.append("  }");
        body.append("}");
        return body.toString();
    }
}
