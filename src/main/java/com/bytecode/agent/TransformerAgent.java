package com.bytecode.agent;

import javassist.CtClass;

/**
 * 定义通用的处理流程
 */
public interface TransformerAgent {
    CtClass transform(CtClass ctClass, String className, ClassLoader loader);
}
