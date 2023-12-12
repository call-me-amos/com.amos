package com.test.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test2 {
    private static final Pattern PATTERN_HOUSE = Pattern.compile("毛坯");
    private static final Pattern PATTERN_INIT = Pattern.compile("农村");

    public static void main(String[] args) throws ParseException {
        Date date = parse("2023-11-16 "+"09:00:00", "yyyy-MM-dd HH:mm:ss");

        System.out.println("==" + date.getTime());

        String f = format(new Date(), "yyyy-MM-dd");
        System.out.println("f="+f);

    }

    public static Date parse(String text, String format) {
        try {
            if (format != null && !"".equals(format) && text != null) {
                SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
                return formatter.parse(text);
            }
        } catch (Exception var4) {
        }

        return null;
    }
    public static String format(Date date, String format) {
        try {
            if (format != null && !"".equals(format) && date != null) {
                SimpleDateFormat formatter = new SimpleDateFormat(format);
                return formatter.format(date);
            }
        } catch (Exception var3) {
        }

        return null;
    }


}
