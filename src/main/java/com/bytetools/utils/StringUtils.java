package com.bytetools.utils;

public class StringUtils {

    public static String dellNull(String queryString) {
        return null == queryString ? "" : queryString.trim();
    }

    public static boolean isEmpty(String packageName) {
        return null == packageName ? true : packageName.isEmpty();
    }
}
