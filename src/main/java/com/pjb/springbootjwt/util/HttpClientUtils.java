package com.pjb.springbootjwt.util;

import com.pjb.springbootjwt.common.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

public class HttpClientUtils {

    /**
     * 向目的URL发送post请求
     * @param url       目的url
     * @param params    发送的参数
     * @return  AdToutiaoJsonTokenData
     */
    public static String sendPostRequest(String url, MultiValueMap<String, String> params){
        RestTemplate client = new RestTemplate();
        //新建Http头，add方法可以添加参数
        HttpHeaders headers = new HttpHeaders();
        //设置请求发送方式
        HttpMethod method = HttpMethod.POST;
        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        //执行HTTP请求，将返回的结构使用String 类格式化（可设置为对应返回值格式的类）
        ResponseEntity<String> response = client.exchange(url, method, requestEntity,String .class);

        return response.getBody();
    }

    /**
     * 向目的URL发送get请求
     * @param url       目的url
     * @param params    发送的参数
     * @param headers   发送的http头，可在外部设置好参数后传入
     * @return  String
     */
    public static Result sendGetRequest(String url, MultiValueMap<String, Object> params, HttpHeaders headers){
        RestTemplate client = new RestTemplate();

        HttpMethod method = HttpMethod.GET;
        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_JSON);
        //将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity(params, headers);
        //执行HTTP请求，将返回的结构使用String 类格式化
        ResponseEntity<Result> response = client.exchange(url, method, requestEntity, Result.class);

        return response.getBody();
    }

    public static Object sendGetRequestV2(String url, LinkedHashMap<String,Object> paramMap, Class<?> responseClass){
        RestTemplate restTemplate = new RestTemplate();
        String paramStr = "?";
        Object[] valueArr = new Object[paramMap.size()];
        int i=0;
        for (Map.Entry<String, Object> entry: paramMap.entrySet()){
            paramStr += entry.getKey()+"={"+(i+1)+"}&";
            valueArr[i] = entry.getValue();
            i++;
        }
        if (paramStr.endsWith("&")){
            paramStr = paramStr.substring(0, paramStr.length()-1);
        }

        String totalUrl = url;
        if (StringUtils.isNotBlank(paramStr)){
            totalUrl = totalUrl.concat(paramStr);
        }
        Object result = restTemplate.getForObject(totalUrl, responseClass, valueArr);

        return result;
    }

    public static Object sendPostRequestV2(String url, LinkedMultiValueMap<String,Object> paramMap, Class<?> responseClass){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result> mapResponseEntity = restTemplate.postForEntity(url, paramMap, Result.class);

        return mapResponseEntity.getBody();
    }


    public static void main(String[] args){
        //-----------------------get example--------------------------------------------------
        LinkedHashMap<String, Object> paraMap = new LinkedHashMap<String, Object>();
        paraMap.put("start", "1");
        paraMap.put("length", "10");
        paraMap.put("status", "2");
        Result result1 = (Result) sendGetRequestV2("http://101.132.176.9:8088/api/AdminEvent/list", paraMap, Result.class);
        System.out.println(result1);


        //------------------------post example----------------------------------------------------
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("loginUserId","896");
        map.add("start","1");
        map.add("length","10");
        String url = "http://101.132.176.9:8088/api/sideCommon/talkTalk/list";
        Result result2 = (Result)sendPostRequestV2(url, map, Result.class);
        System.out.println(result2);
    }
}