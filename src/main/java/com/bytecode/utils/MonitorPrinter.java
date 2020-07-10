package com.bytecode.utils;

import com.bytecode.file.FileIO;

public class MonitorPrinter {

    public static void println(String printlnStr) {
        if (!FileIO.writeFile()) {
            System.out.println(TimeUtil.getTime() + "\033[1;95m DEBUG \033[0m " + printlnStr);
        } else {
            FileIO.write(TimeUtil.getTime() + " " + printlnStr);
        }
    }

    public static void printException(Exception e) {
        e.printStackTrace();
    }

    public static void printMonitorException(Exception e) {
        if(!FileIO.writeFile()){
            e.printStackTrace();
        }else{
            e.printStackTrace(FileIO.getPrintStream());
        }

    }
}
