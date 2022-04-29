package com.example.oversell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
public class OverSellApplication {
    public static void main(String[] args) {
        SpringApplication.run(OverSellApplication.class, args);
    }
}
