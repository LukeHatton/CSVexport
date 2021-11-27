package com.example.csvexport;

import com.example.csvexport.bean.TestTable;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@SpringBootApplication
@MapperScan("com.example.csvexport.mapper")
public class CsVexportApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsVexportApplication.class, args);
    }

    @Bean("csvQueue")
    public Queue<List<? extends TestTable>> getQueue() {
        return new ConcurrentLinkedQueue<>();
    }

}
