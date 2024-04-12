package com.test.smart;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class CheckTypeEnumTest {
    public static void main(String[] args) {
        Map<String, String> map =Maps.newHashMap();
        map.put("意图", "currentIntention");
        map.put("多意图", "currentIntentionList");
        map.put("本轮询问槽位", "currentAskSlot");

        for (CheckTypeEnum e : CheckTypeEnum.values()) {
            map.put("槽位-"+e.toString(), e.getMsg());
            map.put(e + "-询问次数", e.getMsg());
        }

        System.out.println(JSONObject.toJSONString(map));
    }
}
