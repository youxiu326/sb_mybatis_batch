package com.youxiu326;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = "com.youxiu326.dao")
public class SbMybatisBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbMybatisBatchApplication.class, args);
    }

}
