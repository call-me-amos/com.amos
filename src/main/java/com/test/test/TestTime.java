package com.test.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.to8to.common.util.DateUtils;
import org.apache.poi.ss.usermodel.DateUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class TestTime {
    public static void main(String[] args) throws ParseException {

        System.out.println(getRecentNoResponseTime(36000));
    }

    /**
     *
     * 给定多个时间段，找到指定时间最接近的时间点
     */
    public static int getRecentNoResponseTime(int noResponseTime) throws ParseException {
        if (noResponseTime <= 0){
            return noResponseTime;
        }
        long currentTimeMillis = System.currentTimeMillis();
        long current = currentTimeMillis + noResponseTime * 1000L;

        String configStr = "[[\"9:00\",\"12:00\"],[\"12:00\",\"21:00\"]]";
        List<JSONArray> jsonArrays = JSON.parseArray(configStr, JSONArray.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String yyMMdd = sdf.format(new Date());
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (JSONArray jsonArray : jsonArrays) {
            long minDate = sdf2.parse(yyMMdd + " " +  jsonArray.getString(0)).getTime();
            long maxDate = sdf2.parse(yyMMdd + " " +  jsonArray.getString(1)).getTime();
            if (current < minDate){
                return (int) ((minDate - currentTimeMillis)/1000L);
            }

            if (current <= maxDate){
                return noResponseTime;
            }
        }

        // 第二天的第一个时间(延迟时间默认不会超过第二天的初始时间)
        long minDateForNextDay = sdf2.parse(yyMMdd + " " +  jsonArrays.get(0).getString(0)).getTime() + 24 * 3600 * 1000L;
        return (int) ((minDateForNextDay - currentTimeMillis)/1000L);
    }

}
