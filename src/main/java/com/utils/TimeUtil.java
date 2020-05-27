package com.bytetools.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    private static SimpleDateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");

    public static String getTime() {
        return DATA_FORMAT.format(new Date());
    }
}
