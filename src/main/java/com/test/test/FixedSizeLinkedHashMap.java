package com.test.test;

import java.util.LinkedHashMap;
import java.util.Map;

public class FixedSizeLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    private int maxSize;

    public FixedSizeLinkedHashMap(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }

    public static void main(String[] args) {
        FixedSizeLinkedHashMap<String, Integer> map = new FixedSizeLinkedHashMap<>(3);
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        System.out.println(map); // 输出：{A=1, B=2, C=3}

        map.put("D", 4);
        map.put("E", 2);
        map.put("F", 3);
        System.out.println(map); // 输出：{B=2, C=3, D=4}，A 已被删除
    }
}
