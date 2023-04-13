package com.andy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.andy.*")
public class LuceneindexApplication {
    //程序入口
    public static void main(String[] args) {
        SpringApplication.run(LuceneindexApplication.class, args);
    }
}
