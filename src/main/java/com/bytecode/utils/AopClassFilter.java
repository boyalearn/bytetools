package com.bytecode.utils;

import com.bytecode.config.ConfigUtils;

public class AopClassFilter {
    public static boolean include(String className,String methodName){
        return ConfigUtils.shouldAppendSpendTimeCode(StringUtils.getPackageName(className),StringUtils.getClassName(className),methodName);
    }
}
