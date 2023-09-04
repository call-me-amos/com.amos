package com.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    @RequestMapping("sayHello")
    @ResponseBody
    public String sayHello(@RequestBody  String name){
        System.out.println("enter method" + name);

        System.out.println("leave method");
        return "====";
    }
}
