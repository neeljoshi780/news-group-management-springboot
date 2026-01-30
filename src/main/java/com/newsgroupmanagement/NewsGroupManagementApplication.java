package com.newsgroupmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class NewsGroupManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsGroupManagementApplication.class, args);
    }

}
