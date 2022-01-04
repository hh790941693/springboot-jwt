package com.pjb.springbootjwt.common.oss.qiniu;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 七牛云OSS配置文件
 */
@Component
@ConfigurationProperties(prefix = "qiniu")
@Data
public class QiniuConfig {

    private String accessKey;
    private String secretKey;
    private String bucket;
    private String domainPath;
}
