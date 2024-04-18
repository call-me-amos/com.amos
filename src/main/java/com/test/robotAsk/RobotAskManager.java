package com.test.robotAsk;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.test.http.HttpUtils;
import org.apache.http.entity.ContentType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RobotAskManager {
    /**
     * 固定模板id
     */
    private static final String templateId = "60";
    private static final String ticket = "";
//            "?uid=20678&ticket=AWAMY52PNGc1k8Nvwm9Al0TrPqqemP8hQvGrnwlKVee3AnroX4IO1jHZEPDHT2EvZu6t8JtsW5txWWnDkLvMWbeLf4Cclu4YiGw4AnXnOwXwJQDg1CE9pjUMEiMmV2q5&appName=operat-tools&refsrc=%2F"
//            ;
    private static final String url_pre = "http://10.4.42.48:40121/";
    //private static final String url_pre = "https://test-apigw.to8to.com/cgi/";

    private static HttpUtils httpUtils = new HttpUtils();
    public static JSONObject queryContentByChatIdAndCheckTypeCode(String checkTypeCode){
        String url = url_pre + "tls/smartChat/oms/findByTemplateIdAndCheckTypeCode" + ticket;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("templateId", templateId);
        paramMap.put("askSlot", checkTypeCode);
        JSONObject httpResult = httpUtils.sendPostRequest(url, paramMap,
                new HashMap<>(), new TypeReference<JSONObject>() {}, ContentType.APPLICATION_JSON);
        return httpResult;
    }

    public static JSONObject createOrUpdateRobotAskId(Integer robotAskId, String checkTypeCode,
                                                      List<JSONObject> replyList,
                                                      List<JSONObject> defaultReplyList,
                                                      List<JSONObject> noResponseList){
        String url = url_pre + "tls/smartChatRobotAsk/createOrUpdate" + ticket;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("relateTemplateId", templateId);
        paramMap.put("id", robotAskId);
        paramMap.put("checkTypeCode", checkTypeCode);
        paramMap.put("type", 0);

        paramMap.put("noResponseList", noResponseList);
        paramMap.put("defaultReplyList", defaultReplyList);
        paramMap.put("replyList", replyList);

        Map<String, Object> data = new HashMap<>();
        data.put("data", paramMap);


        Map<String, String> header = new HashMap<>();
        header.put("accountId", "123456789");
        header.put("uid", "1234567890");
        JSONObject httpResult = httpUtils.sendPostRequest(url, data,
                header, new TypeReference<JSONObject>() {}, ContentType.APPLICATION_JSON);
        return httpResult;
    }

    public static JSONObject effectOrInvalid(Integer status, List<Integer> ids){
        String url = url_pre + "tls/smartChatRobotAsk/effectOrInvalid" + ticket;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("status", status);
        paramMap.put("ids", ids);
        JSONObject httpResult = httpUtils.sendPostRequest(url, paramMap,
                new HashMap<>(), new TypeReference<JSONObject>() {}, ContentType.APPLICATION_JSON);
        return httpResult;
    }
}
