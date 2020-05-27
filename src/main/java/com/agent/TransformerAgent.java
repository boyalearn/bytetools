package com.bytetools.agent;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

/**
 * 定义通用的处理流程
 */
public interface TransformerAgent {
    CtClass transform(CtClass ctClass, String className, ClassLoader loader);

    boolean shouldTransform(CtClass ctClass, String className);
}
