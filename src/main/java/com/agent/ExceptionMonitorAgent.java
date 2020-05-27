/*
 * Copyright © Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 * Description: ExceptionAgent
 * Author: zWX827285
 * Create: 2020/5/20
 */

package com.bytetools.agent;

import com.bytetools.config.ConfigUtils;
import com.bytetools.entity.FilterEntity;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

/**
 * @author zWX827285
 * @version 1.0.0 2020/5/20
 * @see
 * @since PSM 1.0.5
 */
public class ExceptionMonitorAgent implements TransformerAgent {

    @Override
    public CtClass transform(CtClass ctClass, String className, ClassLoader loader) {
        CtMethod[] methods = ctClass.getDeclaredMethods();
        for (CtMethod method : methods) {

            boolean isExclude = false;
            if (!ConfigUtils.getExcludeFilters().isEmpty()) {
                for (FilterEntity filterEntity : ConfigUtils.getExcludeFilters()) {
                    if (filterEntity.getMethodName().equals(method.getName())) {
                        isExclude = true;
                    }
                }

            }
            if (isExclude) {
                continue;
            }
            boolean isInclude = true;
            if (!ConfigUtils.getIncludeFilters().isEmpty()) {
                isInclude = false;
                for (FilterEntity filterEntity : ConfigUtils.getIncludeFilters()) {
                    if (filterEntity.getMethodName().equals(method.getName())) {
                        isInclude = true;
                    }
                }
            }
            if (!isInclude) {
                continue;
            }
            try {
                CtClass[] parameterTypes = method.getParameterTypes();
                Integer paramIndex = null;
                for (int i = 0; i < parameterTypes.length; i++) {
                    if (parameterTypes[i].isArray()) {
                        continue;
                    }
                    CtClass paramType = parameterTypes[i];
                    if (paramType.getSimpleName().equals("Exception")) {
                        paramIndex = i;
                        break;
                    }
                    if (null != paramType.getSuperclass()) {
                        if (paramType.getSuperclass().getSimpleName().equals("Exception") || paramType.getSuperclass()
                            .getSimpleName()
                            .equals("Throwable")) {
                            paramIndex = i;
                            break;
                        }
                    }

                }
                if (null != paramIndex) {
                    method.insertBefore("{$" + (paramIndex + 1) + ".printStackTrace();}");
                }
            } catch (Exception e) {
                System.out.println("Change Method Exception");
            }
            modifyMethod(ctClass, method);
        }
        System.out.println(
            "exception transform monitor " + ctClass.getPackageName() + "." + ctClass.getSimpleName());
        return ctClass;
    }

    private void modifyMethod(CtClass ctClass, CtMethod method) {
        CtMethod newMethod = null;
        String oldName = "";
        try {

            if (method.getExceptionTypes().length > 0) {
                oldName = method.getName();
                String newName = oldName + "_exception";
                newMethod = CtNewMethod.copy(method, oldName, ctClass, null);
                newMethod.setName(newName);
                ctClass.addMethod(newMethod);
                StringBuilder body = new StringBuilder();
                body.append("{");
                body.append("try{");
                body.append("return " + newName + "($$); ");
                body.append("}catch(Exception e){");
                body.append("e.printStackTrace();");
                body.append("throw e;");
                body.append("}");
                body.append("}");
                method.setBody(body.toString());
            }
        } catch (Exception e) {
            if (null != newMethod) {
                rollbackMethod(ctClass, newMethod, method, oldName);
            }
        }
    }

    private void rollbackMethod(CtClass ctClass, CtMethod newMethod, CtMethod method, String oldName) {
        try {
            ctClass.removeMethod(method);
            newMethod.setName(oldName);
        } catch (Exception e) {
            System.out.println("change method error " + ctClass.getPackageName() + "." + oldName);
        }
    }

    @Override
    public boolean shouldTransform(CtClass ctClass, String className) {
        return true;
    }
}
