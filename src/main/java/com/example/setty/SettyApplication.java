package com.example.setty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.setty")
public class SettyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SettyApplication.class, args);
    }

}
