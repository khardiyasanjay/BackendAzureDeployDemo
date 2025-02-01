package com.backend.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.util.List;

@SpringBootApplication
@EnableCaching
public class Main{

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


}