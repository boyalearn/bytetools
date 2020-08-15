package com.bytecode.file;

import com.bytecode.config.ConfigUtils;
import com.bytecode.utils.StringUtils;

import java.io.*;

public class FileIO {

    private static String FILENAME = ConfigUtils.getLogFileName();

    public static void write(String context) {
        synchronized (FILENAME) {
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
    }

    public static boolean writeFile() {
        if (StringUtils.isEmpty(FILENAME)) {
            return false;
        }
        return true;
    }

    public static synchronized PrintStream getPrintStream() {
        synchronized (FILENAME) {
            File file = new File(FILENAME);

            try {
                return new PrintStream(new FileOutputStream(file, true));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
