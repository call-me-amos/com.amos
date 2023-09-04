package com.test.test;

import com.xkzhangsan.time.formatter.DateTimeFormatterUtil;
import com.xkzhangsan.time.nlp.TimeNLPUtil;

import java.util.List;


public class TimeNLP {
    public static void main(String[] args) {
        List<com.xkzhangsan.time.nlp.TimeNLP> timeNLPList = TimeNLPUtil.parse("下个月");
        System.out.println(DateTimeFormatterUtil.formatToDateTimeStr(timeNLPList.get(0).getTime()) + "-"
                + timeNLPList.get(0).getIsAllDayTime());

        timeNLPList = TimeNLPUtil.parse("这周末");
        System.out.println(DateTimeFormatterUtil.formatToDateTimeStr(timeNLPList.get(0).getTime()) + "-"
                + timeNLPList.get(0).getIsAllDayTime());

        timeNLPList = TimeNLPUtil.parse("4-6月份");
        System.out.println(DateTimeFormatterUtil.formatToDateTimeStr(timeNLPList.get(0).getTime()) + "-"
                + timeNLPList.get(0).getIsAllDayTime());

        timeNLPList = TimeNLPUtil.parse("明年清明节后");
        System.out.println(DateTimeFormatterUtil.formatToDateTimeStr(timeNLPList.get(0).getTime()) + "-"
                + timeNLPList.get(0).getIsAllDayTime());

        timeNLPList = TimeNLPUtil.parse("下个礼拜");
        System.out.println(DateTimeFormatterUtil.formatToDateTimeStr(timeNLPList.get(0).getTime()) + "-"
                + timeNLPList.get(0).getIsAllDayTime());

        timeNLPList = TimeNLPUtil.parse("明年");
        System.out.println(DateTimeFormatterUtil.formatToDateTimeStr(timeNLPList.get(0).getTime()) + "-"
                + timeNLPList.get(0).getIsAllDayTime());

        timeNLPList = TimeNLPUtil.parse("这月底");
        System.out.println(DateTimeFormatterUtil.formatToDateTimeStr(timeNLPList.get(0).getTime()) + "-"
                + timeNLPList.get(0).getIsAllDayTime());
    }

}