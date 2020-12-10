package com.pjb.springbootjwt.zhddkk.util;

import com.alibaba.druid.util.StringUtils;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.entity.PageEntity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;


public class ServiceUtil {
    /**
     * 存储文件.
     * 
     * @param request
     * @param savePath
     * @param fileObj
     * @param filename
     * @return
     */
    public static String storeFile(HttpServletRequest request, String savePath, MultipartFile fileObj,
        String filename) {
        
        if (fileObj.getSize() == 0 || StringUtils.isEmpty(fileObj.getOriginalFilename())) {
            return CommonConstants.FAIL;
        }
        
        File savefile = new File(savePath);
        if (!savefile.exists() && !savefile.isDirectory()) {
            savefile.mkdirs();
        }
        
        FileOutputStream out = null;
        InputStream in = null;
        String result = "";
        if (StringUtils.isEmpty(filename)) {
            filename = fileObj.getOriginalFilename();
        }
        try {
            out = new FileOutputStream(savePath + File.separator + filename);
            in = fileObj.getInputStream();
            byte buffer[] = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            result = CommonConstants.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            result = CommonConstants.FAIL;
        }
        return result;
    }
    
    /**
     * 获取websocket的地址.
     * 
     * @param configMap
     * @return
     */
    public static String getWebsocketIp(Map<String, String> configMap) {
        String readIpFrom = "config";
        String serverIp = configMap.get("webserver.ip");
        if (StringUtils.isEmpty(serverIp)) {
            readIpFrom = "wlan0";
            serverIp = IpUtil.getNamedIp(readIpFrom);
            if (StringUtils.isEmpty(serverIp)) {
                readIpFrom = "eth0";
                serverIp = IpUtil.getNamedIp(readIpFrom);
            }
            if (StringUtils.isEmpty(serverIp)) {
                readIpFrom = "lo";
                serverIp = IpUtil.getNamedIp(readIpFrom);
            }
        }
        
        return serverIp;
    }
    
    /**
     * 根据前端传入的页码等信息算出数据库分页的start和limit.
     *
     * @param curPage 当前页码
     * @param numPerPage 每页显示条数
     * @return 数据库分页
     */
    public static PageEntity buildPage(int curPage, int numPerPage) {
        int start = 0;
        int limit = numPerPage;
        if (curPage == 0 || curPage == 1) {
            start = 0;
        } else {
            start = (curPage - 1) * numPerPage;
        }
        PageEntity p = new PageEntity();
        p.setStart(start);
        p.setLimit(limit);
        
        return p;
    }
}
