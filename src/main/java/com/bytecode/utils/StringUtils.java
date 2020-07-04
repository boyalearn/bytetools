package com.bytecode.utils;

public class StringUtils {
    public static boolean isEmpty(String str) {
        return null == str ? true : str.isEmpty();
    }

    public static String getClassName(String className) {
        int pos = className.replace("/", ".").lastIndexOf(".");
        String name = className.substring(pos + 1);
        return name;
    }

    public static String getPackageName(String className) {
        className=className.replace("/", ".");
        int pos=className.lastIndexOf(".");
        String name = className.substring(0, pos);
        return name;
    }
}
