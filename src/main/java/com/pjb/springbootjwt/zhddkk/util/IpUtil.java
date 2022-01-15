package com.pjb.springbootjwt.zhddkk.util;

import com.pjb.springbootjwt.zhddkk.bean.IpInfoBean;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 获取ip地址工具.
 * 
 * @author hch
 *
 */
public class IpUtil {
    public static List<IpInfoBean> getLocalIPList() {
        List<IpInfoBean> ipList = new ArrayList<IpInfoBean>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface;
            Enumeration<InetAddress> inetAddresses;
            InetAddress inetAddress;
            while (networkInterfaces.hasMoreElements()) {
                networkInterface = networkInterfaces.nextElement();
                inetAddresses = networkInterface.getInetAddresses();
                
                while (inetAddresses.hasMoreElements()) {
                    
                    inetAddress = inetAddresses.nextElement();
                    if (inetAddress != null && inetAddress instanceof Inet4Address) { // IPV4
                        IpInfoBean ifb = new IpInfoBean();
                        // System.out.println(networkInterface.getName()+"-------------"+networkInterface.getDisplayName());
                        String ip = inetAddress.getHostAddress();
                        String name = networkInterface.getName();
                        String displayName = networkInterface.getDisplayName();
                        ifb.setName(name);
                        ifb.setDisplayName(displayName);
                        ifb.setIp(ip);
                        ipList.add(ifb);
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipList;
    }
    
    /**
     * 获取指定网卡的ip地址.
     * 
     * @param networkNamePrifix wlan0,eth0
     * @return
     */
    public static String getNamedIp(String networkNamePrifix) {
        String wifiIp = "";
        List<IpInfoBean> ipList = getLocalIPList();
        if (null != ipList && ipList.size() > 0) {
            for (IpInfoBean iif : ipList) {
                if (iif.getName().startsWith(networkNamePrifix)) {
                    wifiIp = iif.getIp();
                    break;
                }
            }
        }
        return wifiIp;
    }

    /**
     * 检查url连接是否可用
     * @param urlString
     * @param timeout
     * @return
     */
    public static boolean checkUrlStatus(String urlString, int timeout) {
        long lo = System.currentTimeMillis();
        URL url;
        try {
            url = new URL(urlString);
            URLConnection co = url.openConnection();
            co.setConnectTimeout(timeout);
            co.connect();
            return true;
        } catch (Exception e1) {
            url = null;
            return false;
        }
    }
}
