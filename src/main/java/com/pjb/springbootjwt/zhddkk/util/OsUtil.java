package com.pjb.springbootjwt.zhddkk.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

public class OsUtil {
    public static void main(String[] args) {
        System.out.println("系统版本:" + System.getProperty("os.name"));
        System.out.println("java家目录:" + System.getProperty("java.home"));
        System.out.println("jdk版本:" + System.getProperty("java.version"));
        boolean nginxPs = findWindowProcess("nginx.exe");
        System.out.println("nginx进程:" + nginxPs);
    }
    
    public static String queryMysqlVersion(String driver, String url, String user, String password) {
        // 数据库版本
        Connection conn = null;
        String version = "";
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
            DatabaseMetaData metaData = (DatabaseMetaData)conn.getMetaData();
            version = metaData.getDatabaseProductVersion();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != conn) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return version;
    }
    
    public static boolean findWindowProcess(String processName) {
        BufferedReader bufferedReader = null;
        try {
            Process proc = Runtime.getRuntime().exec("tasklist -fi " + '"' + "imagename eq " + processName + '"');
            bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(processName)) {
                    return true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return false;
    }
}
