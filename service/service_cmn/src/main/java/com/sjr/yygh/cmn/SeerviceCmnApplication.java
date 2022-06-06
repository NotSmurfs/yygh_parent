package com.sjr.yygh.cmn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.sjr"})
public class SeerviceCmnApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeerviceCmnApplication.class, args);
    }
}
