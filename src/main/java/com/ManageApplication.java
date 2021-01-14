package com;

import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mapper")
public class ManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageApplication.class, args);
        System.out.println("供应商模块启动成功 ! ! !");
        System.out.println("供应商模块启动成功 ! ! !");
        System.out.println("供应商模块启动成功 ! ! !");
    }
}
