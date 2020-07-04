package com.bytetools.agent;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.bytecode.AccessFlag;

public class TimeMonitorAgent implements TransformerAgent {

    public CtClass transform(CtClass ctClass, String className, ClassLoader loader) {
        try {
            CtMethod[] ctMethods = ctClass.getDeclaredMethods();
            CtMethod newMethod = null;
            String oldName;
            for (CtMethod ctMethod : ctMethods) {
                oldName = ctMethod.getName();
                if ("main".equals(oldName)) {
                    continue;
                }
                if ("toString".equals(oldName)) {
                    continue;
                }
                if ("equals".equals(oldName)) {
                    continue;
                }
                if ("hashCode".equals(oldName)) {
                    continue;
                }

                if (ctMethod.getMethodInfo().getAccessFlags() == AccessFlag.ABSTRACT) {
                    continue;
                }
                String newName = oldName + "_time";
                newMethod = CtNewMethod.copy(ctMethod, oldName, ctClass, null);
                newMethod.setName(newName);

                ctClass.addMethod(newMethod);
                String type = ctMethod.getReturnType().getName();
                StringBuilder body = new StringBuilder();
                body.append("{long start = System.currentTimeMillis();");
                if (!"void".equals(type)) {
                    body.append(type + " result = ");
                }
                body.append(newName + "($$); ");
                body.append("com.bytetools.utils.SystemPrintln.println(\"Call method " + ctClass.getName() + "." + oldName
                    + "() took \" + (System.currentTimeMillis()-start) + \" ms.\");");
                if (!"void".equals(type)) {
                    body.append("return result;");
                }
                body.append("}");
                ctMethod.setBody(body.toString());

            }
            System.out.println("time transform monitor " + ctClass.getPackageName() + "." + ctClass.getSimpleName());
            return ctClass;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
