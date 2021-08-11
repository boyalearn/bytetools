package com.bytetool.agentmain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;

public class AgentMain {
    public static void agentmain(String agentArgs, Instrumentation inst) throws UnmodifiableClassException {
        System.out.println("打印全部加载的类："+agentArgs);
        inst.addTransformer(new Transformer("TestDemo"), true);
        Class[] allLoadedClasses = inst.getAllLoadedClasses();
        for (Class allLoadedClass : allLoadedClasses) {
            if(allLoadedClass.getName().contains("TestDemo")){
                inst.retransformClasses(allLoadedClass);
            }
        }
    }

    private static class Transformer implements ClassFileTransformer {
        private final String targetClassName;

        public Transformer(String targetClassName) {
            this.targetClassName = targetClassName;
        }

        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            className = className.replaceAll("/", ".");
            if (!className.contains(targetClassName)) {
                return null;
            }
            System.out.println("transform: " + className);

            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(loader)); // 将要修改的类的classpath加入到ClassPool中，否则找不到该类
            try {
                CtClass ctClass = classPool.get(className);
                for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
                    if (Modifier.isPublic(ctMethod.getModifiers()) && ctMethod.getName().equals("sayHello")) {
                        // 修改字节码
                        ctMethod.addLocalVariable("begin", CtClass.longType);
                        ctMethod.addLocalVariable("end", CtClass.longType);
                        ctMethod.insertBefore("begin = System.currentTimeMillis();");
                        ctMethod.insertAfter("end = System.currentTimeMillis();");
                        ctMethod.insertAfter("System.out.println(\"方法" + ctMethod.getName() + "耗时\"+ (end - begin) +\"ms\");");
                    }
                }
                ctClass.detach();
                return ctClass.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return classfileBuffer;
        }
    }
}
