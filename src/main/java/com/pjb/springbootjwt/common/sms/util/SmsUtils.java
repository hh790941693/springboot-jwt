package com.pjb.springbootjwt.common.sms.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.pjb.springbootjwt.common.sms.constants.SmsStaticParam;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class SmsUtils {

    private static Logger logger = LoggerFactory.getLogger(SmsUtils.class);

    /**
     * 发送短信验证码
     *
     * @param telephone         接收人手机号
     * @param templateCode      短息模板编号
     * @param templateParamMap  短信参数map
     * @return
     */
    public static SendSmsResponse sendSms(String telephone, String templateCode, Map<String,String> templateParamMap){
        logger.info("开始发送短信验证码,参数如下:");
        logger.info("接收人手机号: "+telephone);
        logger.info("短息模板编号: "+templateCode);
        logger.info("参数json串: "+templateParamMap);

        SendSmsRequest request = null;
        IAcsClient acsClient = null;
        try {
            Map<String, Object> map = buildSmsRequest(telephone, templateCode);
            request = (SendSmsRequest)map.get("request");
            acsClient = (IAcsClient)map.get("client");
        }catch (Exception e){
            e.printStackTrace();
            logger.error("构造短信结构体异常:"+e.getMessage());
            return null;
        }

        SendSmsResponse sendSmsResponse = null;
        JSONObject jsonObj=new JSONObject(templateParamMap);
        String templateParam = jsonObj.toString();
        if (null != request && null != acsClient) {
            request.setTemplateParam(templateParam);
            try {
                sendSmsResponse = acsClient.getAcsResponse(request);
            }catch (Exception e){
                e.printStackTrace();
                logger.error("发送短信异常:"+e.getMessage());
            }
        }
        if (null != sendSmsResponse) {
            logger.info("发送短信响应如下:");
            logger.info("code: "+sendSmsResponse.getCode());
            logger.info("message: "+sendSmsResponse.getMessage());
            logger.info("bizId: "+sendSmsResponse.getBizId());
            logger.info("requestId: "+sendSmsResponse.getRequestId());
        }

        return sendSmsResponse;
    }

    /**
     * 构造短信请求结构体
     *
     * @param telephone      接收人手机号
     * @param templateCode   短信模板编号
     * @return
     * @throws ClientException
     */
    private static Map<String, Object> buildSmsRequest(String telephone, String templateCode) throws ClientException{
        //设置超时时间-可自行调整
        System.setProperty(SmsStaticParam.DEFAULT_CONNECT_TIMEOUT, SmsStaticParam.CONNECT_TIMEOUT);
        System.setProperty(SmsStaticParam.DEFAULT_READ_TIMEOUT, SmsStaticParam.READ_TIMEOUT);
        //初始化ascClient需要的几个参数
        final String product = SmsStaticParam.PRODUCT;//短信API产品名称（短信产品名固定，无需修改）
        final String domain = SmsStaticParam.DOMAIN;//短信API产品域名（接口地址固定，无需修改）
        //替换成你的AK
        final String accessKeyId = SmsStaticParam.ACCESS_KEY_Id;//你的accessKeyId
        final String accessKeySecret = SmsStaticParam.ACCESS_KEY_SECRET;//你的accessKeySecret
        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile(SmsStaticParam.REGINID, accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint(SmsStaticParam.ENDPOINT, SmsStaticParam.REGINID, product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
        request.setPhoneNumbers(telephone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(SmsStaticParam.SIGN_NAME);
        request.setTemplateCode(templateCode);

        Map<String, Object> map = new HashMap<>();
        map.put("request", request);
        map.put("client", acsClient);

        return map;
    }

    /**
     * 从Request对象中获得客户端IP，处理了HTTP代理服务器和Nginx的反向代理截取了ip
     * @param request
     * @return ip
     */
    public static String getClientIp(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String forwarded = request.getHeader("X-Forwarded-For");
        String realIp = request.getHeader("X-Real-IP");

        String ip = "";
        if (realIp == null) {
            if (forwarded == null) {
                ip = remoteAddr;
            } else {
                ip = remoteAddr + "/" + forwarded.split(",")[0];
            }
        } else {
            if (realIp.equals(forwarded)) {
                ip = realIp;
            } else {
                if(forwarded != null){
                    forwarded = forwarded.split(",")[0];
                }
                ip = realIp + "/" + forwarded;
            }
        }
        return ip;
    }

//    public static void main(String[] args) {
//        Map<String,String> templateMap=new HashMap<String,String>();
//        //Map 对象存入 用户名,密码,电话号码
//        templateMap.put("code", "984512");
//        SendSmsResponse response = sendSms("16605141987", SmsStaticParam.REGISTER_VERIFY_CODE, templateMap);
//        if (null != response) {
//            System.out.println("code:"+response.getCode());
//            System.out.println("message:"+response.getMessage());
//            System.out.println("bizId:"+response.getBizId());
//            System.out.println("requestId:"+response.getRequestId());
//        }
//    }
}
