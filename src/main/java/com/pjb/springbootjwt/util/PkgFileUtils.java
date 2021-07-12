package com.pjb.springbootjwt.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PkgFileUtils {

    public static final String PRJ_BASE_PATH = "G:\\workspace\\";

    public static final String STORE_BASE_PATH = "C:\\Users\\huangchaohui\\Desktop\\三菱项目资料\\202107月三菱需求\\package\\";

    public static void main(String[] args){
        run();
    }

    public static void run(){
        File file = new File(STORE_BASE_PATH + "wholeFile.txt");
        BufferedReader reader = null;
        String timeDir = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                if (StringUtils.isBlank(tempString)) {
                    continue;
                }
                copy(PRJ_BASE_PATH + tempString, STORE_BASE_PATH + timeDir + "\\" + tempString);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e1) {
                }
            }
        }
    }

    public static void copy(String source, String dest) {
        InputStream in = null;
        OutputStream out = null;
        String destPathname = dest.substring(0, dest.lastIndexOf("\\"));
        File destPathFile = new File(destPathname);
        if (!destPathFile.exists()) {
            destPathFile.mkdirs();
        }
        try {
            System.out.println(source + "  -->  " + dest);
            in = new FileInputStream(source);
            out = new FileOutputStream(dest);
            IOUtils.copy(in, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }
}
