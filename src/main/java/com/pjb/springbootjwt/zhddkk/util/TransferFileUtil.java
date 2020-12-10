package com.pjb.springbootjwt.zhddkk.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;

public class TransferFileUtil {
    // #transfer.properties
    // #远程服务器IP
    // remote_host=192.168.230.22
    // #端口
    // remote_port=22
    // #远程服务器用户名
    // remote_user=root
    // #远程服务器密码
    // remote_password=hch648788
    //
    // #本地待上传文件
    // local_file=e:\\idea_park.zip
    // #远程服务器存放目录
    // remote_path=/tmp
    //
    // #上传文件前操作
    // pre_upload_cmd=ps -ef | grep java | grep -v grep | awk '{print $2}' | xargs kill -9
    // #上传文件后操作
    // post_upload_cmd=cd /opt/tomcat/bin;./startup.sh&
    private static final HashMap<String, String> MAP = loadAllProperties("transfer.properties");
    
    /**
     * ssh用户登录验证，使用用户名和密码来认证.
     */
    public static void main(String[] args) {
        try {
            Connection con = connect();
            if (null == con) {
                closeStream(con);
                return;
            }
            
            boolean isAuthed = auth(con);
            if (!isAuthed) {
                closeStream(con);
                return;
            }
            
            String preCmd = MAP.get("pre_upload_cmd");
            if (StringUtils.isNotEmpty(preCmd)) {
                execCmd(con, preCmd);
            }
            
            SCPClient scpClient = createSftpClient(con);
            if (null != scpClient) {
                boolean uploadRes = uploadFile(scpClient, MAP.get("local_file"), MAP.get("remote_path"));
                if (uploadRes) {
                    String postCmd = MAP.get("post_upload_cmd");
                    if (StringUtils.isNotEmpty(postCmd)) {
                        execCmd(con, postCmd);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // 连接
    private static Connection connect() {
        String host = MAP.get("remote_host");
        int port = Integer.valueOf(MAP.get("remote_port"));
        System.out.println("开始连接服务器:" + host);
        Connection con = new Connection(host, port);
        try {
            con.connect();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("连接服务器异常:" + e.getMessage());
            con = null;
        }
        return con;
    }
    
    // 验证
    private static Boolean auth(Connection con) {
        String user = MAP.get("remote_user");
        String pass = MAP.get("remote_password");
        System.out.println("开始以" + user + "身份验证");
        boolean res = false;
        try {
            res = con.authenticateWithPassword(user, pass);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("验证身份异常:" + e.getMessage());
            res = false;
        }
        if (res) {
            System.out.println("验证通过!");
        }
        return res;
    }
    
    // 创建客户端
    private static SCPClient createSftpClient(Connection connect) {
        System.out.println("创建客户端对象");
        SCPClient scpClient = null;
        try {
            scpClient = connect.createSCPClient();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("创建客户端对象异常:" + e.getMessage());
        }
        System.out.println("创建客户端结束");
        return scpClient;
    }
    
    // 执行远程命令
    private static void execCmd(Connection con, String cmd) {
        Session session = null;
        InputStream stdout = null;
        BufferedReader br = null;
        System.out.println("开始执行命令:  " + cmd);
        try {
            session = con.openSession();
            session.execCommand(cmd);
            stdout = new StreamGobbler(session.getStdout());
            br = new BufferedReader(new InputStreamReader(stdout));
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(line);
            }
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("执行命令异常:  " + e.getMessage());
        } finally {
            if (null != stdout) {
                try {
                    stdout.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (null != br) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (session != null) {
                try {
                    session.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("执行命令" + cmd + "结束 ");
    }
    
    // 上传文件
    private static boolean uploadFile(SCPClient scpClient, String localFile, String remoteTargetDirectory) {
        boolean res = false;
        try {
            System.out.println("开始上传文件 " + localFile + "--->" + remoteTargetDirectory);
            scpClient.put(localFile, remoteTargetDirectory);
            res = true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("上传文件失败！");
            res = false;
        }
        System.out.println(localFile + "上传完成！");
        return res;
    }
    
    // 关闭流
    private static void closeStream(Connection con) {
        try {
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // 加载配置
    private static HashMap<String, String> loadAllProperties(String fileName) {
        String path = TransferFileUtil.class.getClassLoader().getResource(fileName).getPath();
        
        HashMap<String, String> map = new HashMap<String, String>();
        BufferedReader br = null;
        InputStream is = null;
        try {
            is = new FileInputStream(path);
            br = new BufferedReader(new InputStreamReader(is));
            
            Properties pr = new Properties();
            pr.load(br);
            
            for (Object o : pr.keySet()) {
                if (!map.containsKey(o)) {
                    String value = pr.getProperty(o + "");
                    value = value.trim();
                    map.put(o + "", value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return map;
    }
}
