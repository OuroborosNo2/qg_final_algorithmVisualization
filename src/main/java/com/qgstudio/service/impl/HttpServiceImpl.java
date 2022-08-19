package com.qgstudio.service.impl;

import com.alibaba.fastjson.JSON;
import com.qgstudio.service.HttpService;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
/**
 * @author OuroborosNo2
 * @since 2022-8-17
 */
@Service
public class HttpServiceImpl implements HttpService {

    @Override
    public String sendRequest(String url, Map<String, Object> params){
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);

        postMethod.addRequestHeader("accept", "*/*");
        //设置Content-Type，此处根据实际情况确定
        postMethod.addRequestHeader("Content-Type", "application/json");

        postMethod.setRequestBody(JSON.toJSONString(params));
        try {
            int code = httpClient.executeMethod(postMethod);
            if (code == 200){
                return postMethod.getResponseBodyAsString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
