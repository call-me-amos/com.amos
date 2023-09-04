package com.test.opennl;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseTimeDemo {
    private static final String REGEX_LIB_TIMESTAMP = "\\d{1}月(后|内|之后|以后|之内|以内)|(\\d{1,2})月|(一|二|三|四|五|六|七|八|九|十|十一|十二)月";
    public static void main(String[] args) {

        findAnswerByRegex(REGEX_LIB_TIMESTAMP,"wo 准备  1月之后 也行");
        System.out.println("------------------");
    }
    private static String findAnswerByRegex(String regex, String originText){
        Pattern patternInit = Pattern.compile(regex);
        Matcher matcher = patternInit.matcher(originText);
        if (matcher.find()){
            String preAnswer = matcher.group();
            System.out.println("=======" + preAnswer);
            return preAnswer;
        }
        return null;
    }

    private static final List<String> REGEX_LIB_FOR_TIME = new ArrayList<>();
    static {
        REGEX_LIB_FOR_TIME.add("\\d+天(后|内|之后|以后)");
        REGEX_LIB_FOR_TIME.add("\\d+周(后|内|之后|以后)");
        REGEX_LIB_FOR_TIME.add("\\d+个月(后|内|之后|以后)|\\d+个月");
    }

    public static String processAnswer(String originText){
        String standardAnswer = null;
        // 默认只有装修时间正则表达式
        for(String regex: REGEX_LIB_FOR_TIME){
            standardAnswer = findAnswerByRegex(regex, originText);
            if (null != standardAnswer){
                System.out.println("===" + standardAnswer);
                //break;
            }
        }
        return standardAnswer;
    }

    public static void test(){
        String text = "个月后就是2022年6月5日是周六，再个月就是7月了  这段时间  也行";
        String initRegex = "(下个月|这个月|本月|尽量月底前|半年内|3个月内|3个月后)";
        String monthRegex = "(\\\\d{1,2})月";
        String dayRegex = "(\\\\d{1,2}){日|号}}";
        Pattern patternInit = Pattern.compile(initRegex);
        Matcher matcher = patternInit.matcher(text);
        while (matcher.find()){
            System.out.println(matcher.group());
        }

        if (matcher.find()) {
            String year = matcher.group(1);
        } else {
            System.out.println("未匹配到日期信息");
        }
    }

}
