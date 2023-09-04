package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author amos.tong
 */
@SpringBootApplication
public class SpringBootStarter {
    public static void main(String[] args) {
        System.out.println("======== started");


        SpringApplication.run(SpringBootStarter.class, args);



        System.out.println("======== end");
    }
}
