package com.pjb.springbootjwt.zhddkk.job;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.zhddkk.domain.WsFileDO;
import com.pjb.springbootjwt.zhddkk.service.WsFileService;
import com.pjb.springbootjwt.zhddkk.websocket.WebSocketConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

/***
 * 定时更新表ws_file中url的ip地址
 */
@Configuration
@EnableScheduling
public class WsFileAccessCheckJob {

    private static Logger logger = LoggerFactory.getLogger(WsFileAccessCheckJob.class);

    @Autowired
    private WsFileService wsFileService;

    @Autowired
    private WebSocketConfig webSocketConfig;

    @Scheduled(cron="*/59 * * * * ?")
    public void cronJob(){
        logger.info("定时检查ws_file文件的访问性");
        List<WsFileDO> wsFileList = wsFileService.selectList(new EntityWrapper<WsFileDO>().isNotNull("url"));
        if (null == wsFileList || wsFileList.size() == 0){
            logger.info("没有需要检查的文件");
            return;
        }

        String webserverip = webSocketConfig.getAddress();
        List<WsFileDO> needBatchUpdateList = new ArrayList<>();
        for (WsFileDO wsFileDO : wsFileList){
            String url = wsFileDO.getUrl();
            if (StringUtils.isBlank(url)){
                continue;
            }
            String oldIp = url.substring(url.indexOf("//")+2, url.lastIndexOf(":"));
            if (!oldIp.equals(webserverip)) {
                logger.info("旧文件url:{}", url);
                String newUrl = url.replace(oldIp, webserverip);
                wsFileDO.setUrl(newUrl);
                logger.info("新文件url:{}", newUrl);
                needBatchUpdateList.add(wsFileDO);
            }
        }
        if (needBatchUpdateList.size()>0){
            logger.info("本次更新文件数:{}", needBatchUpdateList.size());
            wsFileService.updateBatchById(needBatchUpdateList, needBatchUpdateList.size());
        }
    }
}
