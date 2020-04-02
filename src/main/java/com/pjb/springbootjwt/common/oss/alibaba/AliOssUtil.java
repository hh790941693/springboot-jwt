package com.pjb.springbootjwt.common.oss.alibaba;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.pjb.springbootjwt.common.utils.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AliOssUtil {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AliOssUtil.class);

    public static String upload(File file){
        logger.info("进入文件上传业务层, file:{}", file);
        if(null == file){
            return null;
        }

        AliOssConfig aliOssConfig = SpringContextHolder.getBean(AliOssConfig.class);
        String endpoint = aliOssConfig.getEndpoint();
        String accessKey = aliOssConfig.getAccessKey();
        String accessSecret = aliOssConfig.getAccessSecret();
        String bucketName = aliOssConfig.getBucketName();
        String rootPath = aliOssConfig.getRootPath();
        String viewUrl = aliOssConfig.getViewUrl();

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dateStr = format.format(new Date());

        OSSClient ossClient = new OSSClient(endpoint,accessKey,accessSecret);
        try {
            //容器不存在，就创建
            if(!ossClient.doesBucketExist(bucketName)){
                ossClient.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }
            //创建文件路径
            String fileUrl = "";
            String filename = file.getName();
            String filePostFix = filename.substring(filename.indexOf("."));
            if (StringUtils.isNotBlank(rootPath)) {
                fileUrl = rootPath+"/"+(dateStr + "/" + UUID.randomUUID().toString().replace("-","")+filePostFix);
            }else{
                fileUrl = dateStr + "/" + UUID.randomUUID().toString().replace("-","")+filePostFix;
            }

            //上传文件
            PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, file));
            //设置权限 这里是公开读
            ossClient.setBucketAcl(bucketName,CannedAccessControlList.PublicRead);
            if(null != result){
                logger.info("==========>OSS文件上传成功,OSS地址："+fileUrl);
                return viewUrl+fileUrl;
            }
        }catch (OSSException oe){
            logger.error(oe.getMessage());
        }catch (ClientException ce){
            logger.error(ce.getMessage());
        }finally {
            //关闭
            ossClient.shutdown();
        }
        return null;
    }
}
