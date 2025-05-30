package com.example.matzipbookserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MatzipbookServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatzipbookServerApplication.class, args);
    }

}