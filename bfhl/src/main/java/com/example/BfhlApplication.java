package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.example", "com.controller"})
public class BfhlApplication {

    public static void main(String[] args) {
        SpringApplication.run(BfhlApplication.class, args);
    }
}
