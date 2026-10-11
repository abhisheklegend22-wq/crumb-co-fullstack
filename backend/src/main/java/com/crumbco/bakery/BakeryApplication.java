package com.crumbco.bakery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BakeryApplication {
    public static void main(String[] args) {
        SpringApplication.run(BakeryApplication.class, args);
    }
}
