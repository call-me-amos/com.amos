package com.test.test;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ChatIdToUid {
    public static void main(String[] args) {

        List<String> chatIds = Arrays.asList(
                "MTIzNjE1OTcjd21Yb29rQ2dBQXJqSTBKN1ZfRUpZX2QySWVKTjZBMkE="
        );
        chatIds.forEach(chatId->{
            String base64ChatId = new String(Base64.getDecoder().decode(chatId));
            System.out.println(base64ChatId.split("#")[0]);
            System.out.println(base64ChatId.split("#")[1]);
        });



        System.out.println("======================================");
        String chatId = new String(Base64.getEncoder().encode(("tongzhiwei#"+"user-01").getBytes()));
        System.out.println("chatId="+ chatId);


        List<String> strList = new ArrayList<>();
        strList.add("1");
        strList.add("2");
        strList.add(0, "345");
        System.out.println("asff"+JSONObject.toJSONString(strList));

    }
}

