package com.example.matzipbookserver;

import com.example.matzipbookserver.global.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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