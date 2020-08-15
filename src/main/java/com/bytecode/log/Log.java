package com.bytecode.log;

import com.bytecode.file.FileIO;
import com.bytecode.utils.TimeUtil;

public class Log {
    public static void log(String log) {
        if (!FileIO.writeFile()) {
            System.out.println(TimeUtil.getTime() + "\033[1;95m DEBUG \033[0m " + log);
        } else {
            FileIO.write(TimeUtil.getTime() + " " + log);
        }
    }

    public static void log(Exception e) {
        e.printStackTrace();
    }
}
