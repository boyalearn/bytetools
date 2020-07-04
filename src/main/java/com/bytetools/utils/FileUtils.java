/*
 * Copyright © Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 * Description: FileUtils
 * Author: zWX827285
 * Create: 2020/5/21
 */

package com.bytetools.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * @author zWX827285
 * @version 1.0.0 2020/5/21
 * @see
 * @since PSM 1.0.5
 */
public class FileUtils {
    public static synchronized void write(String context) {
        try {
            File file = new File("D:/ByteCodeTest/time.log");

            if(!file.exists())
                file.createNewFile();

            FileWriter out = new FileWriter(file, true);
            BufferedWriter bw= new BufferedWriter(out);
            bw.write(context);
            bw.newLine();

            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
