package com.pjb.springbootjwt.zhddkk.util;

import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ExcuteLinuxCmdUtil {

	private static final Log logger = LogFactory.getLog(ExcuteLinuxCmdUtil.class);
	
	public static String runLinuxCmd(String command){
		logger.info("start to call linux cmd:" + command);
		Scanner input = null;
		String logresult = "";
		String rtnresult = "";
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command);
			try {
				//等待命令执行完成
				process.waitFor(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			InputStream is = process.getInputStream();
			input = new Scanner(is);
			while (input.hasNextLine()) {
				rtnresult += input.nextLine() + "\n";
			}
			logresult = command + "\n" + rtnresult; //加上命令本身，打印出来
			logger.error("linux cmd result:" + logresult);
		} catch (Exception e){
			logger.error("failed to call linux cmd,e:" + e.getMessage());
		} finally {
			if (input != null) {
				input.close();
			}
			if (process != null) {
				process.destroy();
			}
		}
		return rtnresult;
	}
	
	public static String runLinuxCmd(String[] command,boolean printLog){
		if (printLog) {
			logger.info("start to call linux cmd:" + StringUtils.join(command, " "));
		}
		Scanner input = null;
		String logresult = "";
		String rtnresult = "";
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command);
			try {
				//等待命令执行完成
				process.waitFor(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			InputStream is = process.getInputStream();
			input = new Scanner(is);
			while (input.hasNextLine()) {
				rtnresult += input.nextLine() + "\n";
			}
			logresult = StringUtils.join(command, " ") + "\n" + rtnresult; //加上命令本身，打印出来
			if (printLog) {
				logger.info("linux cmd result:" + logresult);
			}
		} catch (Exception e){
			if (printLog) {
				logger.error("failed to call linux cmd,e:" + e.getMessage());
			}
		} finally {
			if (input != null) {
				input.close();
			}
			if (process != null) {
				process.destroy();
			}
		}
		return rtnresult;
	}
	
	public static String executeLinuxCmd(String cmd) {
        logger.info("start to call linux cmd:" + cmd);
        Runtime run = Runtime.getRuntime();
        try {
             Process process = run.exec(cmd);
             InputStream in = process.getInputStream();
             //BufferedReader bs = new BufferedReader(new InputStreamReader(in));
             StringBuffer out = new StringBuffer();
             byte[] b = new byte[8192];
             for (int n=0; (n = in.read(b)) != -1;) {
                 out.append(new String(b, 0, n));
             }
             in.close();
             logger.info("linux cmd result:" + out.toString());
             // process.waitFor();
             process.destroy();
             return out.toString();
        } catch (Exception e) {
        	logger.error("failed to call linux cmd,e:" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
