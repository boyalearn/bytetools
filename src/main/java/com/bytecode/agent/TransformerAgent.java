package com.bytecode.agent;

import javassist.CtClass;

/**
 * 定义通用的处理流程
 */
public interface TransformerAgent {
    CtClass transform(String className, ClassLoader loader) throws Exception;
}
