package com.example.tokendemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.example.tokendemo.mapper")
@ComponentScan({"com.example"})
public class TokendemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TokendemoApplication.class, args);
    }

}
