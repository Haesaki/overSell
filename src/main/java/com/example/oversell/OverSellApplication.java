package com.example.oversell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = {"com.example.oversell.tkmapper"})
//@ComponentScan(basePackages = {"com.example.oversell.mapper"})
public class OverSellApplication {
    public static void main(String[] args) {
        SpringApplication.run(OverSellApplication.class, args);
    }
}
