package com.test.robotAsk;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.test.excel.ForRuleConditionMain;
import com.test.http.HttpUtils;
import org.apache.http.entity.ContentType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RobotAskManager {


    private static HttpUtils httpUtils = new HttpUtils();
    public static JSONObject queryContentByChatIdAndCheckTypeCode(String checkTypeCode){
        String url = ForRuleConditionMain.url_pre + "tls/smartChat/oms/findByTemplateIdAndCheckTypeCode" + ForRuleConditionMain.ticket;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("templateId", ForRuleConditionMain.TEMPLATE_ID);
        paramMap.put("askSlot", checkTypeCode);
        JSONObject httpResult = httpUtils.sendPostRequest(url, paramMap,
                new HashMap<>(), new TypeReference<JSONObject>() {}, ContentType.APPLICATION_JSON);
        return httpResult;
    }

    public static JSONObject createOrUpdateRobotAskId(Integer robotAskId, String checkTypeCode,
                                                      List<JSONObject> replyList,
                                                      List<JSONObject> defaultReplyList,
                                                      List<JSONObject> noResponseList){
        String url = ForRuleConditionMain.url_pre + "tls/smartChatRobotAsk/createOrUpdate" + ForRuleConditionMain.ticket;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("relateTemplateId", ForRuleConditionMain.TEMPLATE_ID);
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
        String url = ForRuleConditionMain.url_pre + "tls/smartChatRobotAsk/effectOrInvalid" + ForRuleConditionMain.ticket;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("status", status);
        paramMap.put("ids", ids);
        JSONObject httpResult = httpUtils.sendPostRequest(url, paramMap,
                new HashMap<>(), new TypeReference<JSONObject>() {}, ContentType.APPLICATION_JSON);
        return httpResult;
    }
}
