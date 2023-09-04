package com.test.test;

import io.netty.util.internal.ThrowableUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Test2 {
    public static void main(String[] args) throws ParseException {

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String hourAndMinutes = "2023-08-30 18:00";
        System.out.println("====="+sdf2.parse(hourAndMinutes).getTime()/1000L);

    }
}
// 2023-08-30 18:00
// 2023-07-31 18:00:00  yyyy-MM-dd HH:dd
// 2023-08-30 18:00:00  yyyy-MM-dd HH:mm
