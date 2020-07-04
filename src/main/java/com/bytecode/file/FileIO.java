package com.bytecode.file;

import com.bytecode.config.ConfigUtils;
import com.bytecode.utils.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class FileIO {

    private static String FILENAME = ConfigUtils.getLogFileName();

    public static synchronized void write(String context) {
        try {
            File file = new File(FILENAME);

            if (!file.exists())
                file.createNewFile();

            FileWriter out = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(out);
            bw.write(context);
            bw.newLine();

            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean writeFile() {
        if (StringUtils.isEmpty(FILENAME)) {
            return false;
        }
        return true;
    }
}
