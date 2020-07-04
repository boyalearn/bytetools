package com.bytecode.utils;

public class MonitorPrinter {

    public static void println(String printlnStr) {
        System.out.println(TimeUtil.getTime() + "\033[1;95m DEBUG \033[0m " + printlnStr);
    }

    public static void printException(Exception e) {
        e.printStackTrace();
    }

    public static void printMonitorException(Exception e){
        e.printStackTrace();
    }
}
