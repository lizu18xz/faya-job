package com.fayayo.job.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * 管理端启动类
 * */
//@ComponentScan("com.fayayo.job")
@SpringBootApplication
@EntityScan("com.fayayo.job.entity")
public class ManagerApp {

    public static void main(String[] args) {

        SpringApplication.run(ManagerApp.class);

    }

}
