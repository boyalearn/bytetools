package com.bytecode.agent;

import javassist.*;

public class CglibAopAgent implements TransformerAgent {

    public static final String CLASS_NAME="DynamicAdvisedInterceptor";

    @Override
    public CtClass transform(CtClass ctClass, String className, ClassLoader loader) {
        CtMethod[] methods = ctClass.getDeclaredMethods();
        for (CtMethod method : methods) {
            if (method.getName().contains("intercept")) {
                System.out.println(method.getName());

                try {
                    changeMethodToMonitorSpendTime(ctClass, method);
                } catch (CannotCompileException e) {
                    e.printStackTrace();
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
        return ctClass;
    }

    public void changeMethodToMonitorSpendTime(CtClass ctClass, CtMethod ctMethod) throws CannotCompileException, NotFoundException {
        String oldName = ctMethod.getName();
        String newName = oldName + "__doTimeMethod";
        CtMethod newMethod = CtNewMethod.copy(ctMethod, oldName, ctClass, null);
        newMethod.setName(newName);
        ctClass.addMethod(newMethod);
        //构造原始方法的方法体
        ctMethod.setBody(buildMonitorTimeAopMethodBody(ctMethod, newName));
    }

    private String buildMonitorTimeAopMethodBody(CtMethod ctMethod, String newName) throws NotFoundException, CannotCompileException {
        StringBuilder methodBody = new StringBuilder();
        methodBody.append("{");
        methodBody.append("  long __startTime=System.currentTimeMillis();");
        methodBody.append("try{");
        methodBody.append("  return " + newName + "($$);");
        methodBody.append("}catch(Exception e){");
        methodBody.append("  throw e;");
        methodBody.append("}finally{ ");
        methodBody.append("  String __className=$1.getClass().getName();");
        methodBody.append(" System.out.println(__className);");
        methodBody.append("  String __methodName=((java.lang.reflect.Method)$2).getName();");
        methodBody.append("  if(com.bytecode.utils.AopClassFilter.include(__className,__methodName))");
        methodBody.append("    com.bytecode.utils.MonitorPrinter.println(__className+\".\"+__methodName+\"() call spend \"+(System.currentTimeMillis()-__startTime)+\" ms.\");");
        methodBody.append("}");
        methodBody.append("}");
        System.out.println(methodBody.toString());
        return methodBody.toString();
    }
}
