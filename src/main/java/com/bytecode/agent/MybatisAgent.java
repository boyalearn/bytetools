package com.bytecode.agent;

import javassist.CtClass;

public class MybatisAgent implements TransformerAgent{
    public final String CLASS_NAME="CachingExecutor";

    public final String METHOD_NAME="org.apache.ibatis.executor.CachingExecutor#query(org.apache.ibatis.mapping.MappedStatement, java.lang.Object, org.apache.ibatis.session.RowBounds, org.apache.ibatis.session.ResultHandler, org.apache.ibatis.cache.CacheKey, org.apache.ibatis.mapping.BoundSql)";

    @Override
    public CtClass transform(CtClass ctClass, String className, ClassLoader loader) {
        return null;
    }
}
