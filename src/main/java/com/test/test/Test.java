package com.test.test;

import com.test.excel.ForRuleConditionMain;

import java.util.Arrays;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        test(" (((多意图 in (用户投诉、用户辱骂、表达重复联系)))");

    }

    public  static void test (String part){
       String abc = "123456789";
        System.out.println(abc.substring(4));
    }
    private static String findExpressName(String logicName) {
        String temp = logicName.replace("(","").replace(")","").trim();
        for (Map.Entry<String, String> entry : ForRuleConditionMain.DEFAULT_PARAMETER_MAP.entrySet()) {
            if (entry.getValue().equals(temp)) {
                String key = entry.getKey();
                return logicName.replace(temp, key);
            }
        }
        return "ERROR： " + logicName;
    }


    // java判断str包含字符A的个数
    public static int countOccurrences(String str, char targetChar) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == targetChar) {
                count++;
            }
        }
        return count;
    }

    // 判断字符串包含指定字符的个数，如果不够，在字符串后面追加缺失个数
    public static String appendCharacters(int direction, String str, char targetChar, int count) {
        if(count == 0){
            return str;
        }
        StringBuilder sb = new StringBuilder();
        // 前面追加
        if(1== direction){
            for (int i = 0; i < count; i++) {
                sb.append(targetChar);
            }
            sb.append(str);
        } else {
            // 后面追加
            sb.append(str);
            for (int i = 0; i < count; i++) {
                sb.append(targetChar);
            }
        }
        return sb.toString();
    }

    private static String getStringWithSingleQuotationMark(String value, String expressName) {
        if (expressName.endsWith("_VALUE")
                || Arrays.asList("currentIntention", "currentIntentionList", "currentAskSlot", "skipSlot",
                "(currentIntention", "(currentIntentionList", "(currentAskSlot","(skipSlot").contains(expressName)) {
            value= getStringWithSingleQuotationMark(value);
        }
        return value;
    }
    private static String getStringWithSingleQuotationMark(String value) {
        String temp = value.replace("(", "").replace(")", "");
        String newTemp = "'" + temp + "'";
        value = value.replace(temp, newTemp);
        return value;
    }

    private static String getStringWithNoQuotationMark(String value) {
        String temp = value.replace("(", "").replace(")", "");
        String newTemp = "" + temp + "";
        value = value.replace(temp, newTemp);
        return value;
    }
}
