package com.bytetools.transformer;

import com.bytetools.agent.TransformerAgent;
import com.bytetools.config.ConfigUtils;
import com.bytetools.entity.FilterEntity;
import com.bytetools.filter.AgentFilter;
import com.bytetools.utils.StringUtils;

import java.io.FileOutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.Modifier;
import javassist.NotFoundException;

public class CodeByteTransformer implements ClassFileTransformer {

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
        ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        for (FilterEntity filterEntity : ConfigUtils.getIncludeFilters()) {

            if (!StringUtils.isEmpty(filterEntity.getPackageName()) && !className.startsWith(
                filterEntity.getPackageName().replace(".", "/"))) {
                return null;
            }
            if (!StringUtils.isEmpty(filterEntity.getClassName()) && !className.contains(filterEntity.getClassName())) {
                return null;
            }

        }

        for (FilterEntity filterEntity : ConfigUtils.getExcludeFilters()) {

            if (!StringUtils.isEmpty(filterEntity.getPackageName()) && className.startsWith(
                filterEntity.getPackageName().replace(".", "/"))) {
                return null;
            }
            if (!StringUtils.isEmpty(filterEntity.getClassName()) && className.contains(filterEntity.getClassName())) {
                return null;
            }

        }
        ClassPool pool = ClassPool.getDefault();
        pool.appendClassPath(new LoaderClassPath(loader));
        CtClass ctClass;
        CtClass tmpCtClass = null;
        try {
            ctClass = pool.getCtClass(className.replace("/", "."));
            if (ctClass.isFrozen() || Modifier.isInterface(ctClass.getModifiers()) || Modifier.isAbstract(
                ctClass.getModifiers())
                || Modifier.isAnnotation(ctClass.getModifiers())|| Modifier.isEnum(ctClass.getModifiers())) {
                return null;
            }
            for (TransformerAgent agent : AgentFilter.getTransformerAgent()) {

                    tmpCtClass = agent.transform(ctClass, className, loader);
            }

            if (null != tmpCtClass) {
                try {
                    FileOutputStream fos = new FileOutputStream(
                        "D:/ByteCodeTest/" + tmpCtClass.getSimpleName() + ".class");
                    fos.write(ctClass.toBytecode());
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    System.out.println(
                        " transform class" + tmpCtClass.getPackageName() + "." + tmpCtClass.getSimpleName());
                    return tmpCtClass.toBytecode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (NotFoundException e) {

        }
        return null;
    }

}
