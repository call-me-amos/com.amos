package com.test.test;

import com.test.smart.CheckTypeEnum;

import java.util.LinkedHashMap;

public class Test {
    public static void main(String[] args) {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>(3, 0.75f, true);
        //map.set
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        System.out.println(map); // 输出：{A=1, B=2, C=3}

        map.put("D", 4);
        System.out.println(map); // 输出：{B=2, C=3, D=4}，A 已被删除

        map.get("f");
        System.out.println(map); // 输出：{B=2, C=3, D=4}，A 已被删除

        map.get("A");
        System.out.println(map); // 输出：{B=2, C=3, D=4}，A 已被删除
        map.get("E");
        System.out.println(map); // 输出：{B=2, C=3, D=4}，A 已被删除
        map.put("E", 2);
        map.put("F", 3);
//        map.get("E");
//        System.out.println(map); // 输出：{B=2, C=3, D=4}，A 已被删除

    }
}
