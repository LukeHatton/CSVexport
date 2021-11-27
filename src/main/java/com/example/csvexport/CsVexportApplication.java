package com.example.csvexport;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.csvexport.mapper")
public class CsVexportApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsVexportApplication.class, args);
    }

}
