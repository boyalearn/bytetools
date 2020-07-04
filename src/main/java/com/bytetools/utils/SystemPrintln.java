package com.bytetools.utils;

public class SystemPrintln {

    public static void println(String printlnStr) {
        System.out.println(TimeUtil.getTime() + "\033[1;95m DEBUG \033[0m " + printlnStr);
    }

}
