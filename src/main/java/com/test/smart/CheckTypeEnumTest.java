package com.test.smart;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class CheckTypeEnumTest {
    public static void main(String[] args) {
        Map<String, String> map =Maps.newHashMap();
        for (CheckTypeEnum e : CheckTypeEnum.values()) {
            map.put(e.getMsg(), e.getCode());
        }

        System.out.println(JSONObject.toJSONString(map));
    }
}
