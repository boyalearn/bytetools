package com.bytecode.utils;

import com.bytecode.file.FileIO;

public final class MonitorUtil {
    public static void monitor(String methodName, long spendTime) {
        String log = TimeUtil.getTime() + "\033[1;95m DEBUG \033[0m " + methodName + " execute spend " + spendTime + " ms";

        if (!FileIO.writeFile()) {
            System.out.println(log);
        } else {
            FileIO.write(log);
        }
    }
    public static void printE(Exception e){
        if(!FileIO.writeFile()){
            e.printStackTrace();
        }else{
            e.printStackTrace(FileIO.getPrintStream());
        }
    }
}
