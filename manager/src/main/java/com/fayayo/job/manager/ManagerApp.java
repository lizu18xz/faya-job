package com.fayayo.job.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * 管理端启动类
 * */
@SpringBootApplication
@EntityScan("com.fayayo.job.entity")
public class ManagerApp {

    public static void main(String[] args) {

        SpringApplication.run(ManagerApp.class);

    }

}
