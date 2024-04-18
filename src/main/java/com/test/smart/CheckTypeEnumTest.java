package com.test.smart;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CheckTypeEnumTest {
    public static void main(String[] args) {
        CheckTypeEnum.checkRepetitionMsg();

        System.out.println("================");
    }

    public static void checkCheckTypeCode() {
        Map<String, String> map =Maps.newHashMap();
        for (CheckTypeEnum e : CheckTypeEnum.values()) {
            map.put(e.getMsg(), e.getCode());
        }

        checkTypeCodeList.forEach(msg->{
            if(null == map.get(msg)){
                System.out.println("不存在的槽位： " + msg);
            }
        });

        //System.out.println(JSONObject.toJSONString(map));
    }

    /**
     * 生成槽位名和编码的映射关系
     */
    public static void mapping() {
        Map<String, String> map =Maps.newHashMap();
        for (CheckTypeEnum e : CheckTypeEnum.values()) {
            map.put(e.getMsg(), e.getCode());
        }

        //System.out.println(JSONObject.toJSONString(map));
    }

    private static final List<String> checkTypeCodeList = Arrays.asList(
            "小区地址-暂不方便沟通",
            "自建房小区地址-追问2",
            "城市-追问1",
            "是否交房-暂不方便沟通",
            "需求信息",
            "需求类型-精装房/简装房",
            "房屋面积暂不方便沟通",
            "自建房是否交房",
            "城市-暂不方便沟通",
            "自建房是否交房-追问1",
            "房屋类型",
            "装修用途-追问2",
            "价值点-房屋面积-追问1",
            "自建房小区地址-追问1",
            "小区地址-追问2",
            "电话-追问1",
            "需求类型-追问1",
            "装修用途-追问1",
            "是否交房-追问1",
            "交房时间-暂不方便沟通",
            "房屋面积-追问2",
            "房屋类型_澄清槽位",
            "交房时间-追问1",
            "城市-追问2",
            "自建房小区地址-暂不方便沟通",
            "房屋面积-追问1",
            "电话-追问2",
            "房屋类型_澄清槽位",
            "需求类型-简装",
            "自建房小区地址",
            "装修用途-暂不方便沟通",
            "需求类型-暂不方便沟通",
            "自建房是否交房-暂不方便沟通",
            "小区地址-追问1",
            "需求类型-精装");
}
