package com.honji.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.honji.order.mapper")
public class CcbQueryApplication  {

    public static void main(String[] args) {
        SpringApplication.run(CcbQueryApplication.class, args);

    }


}
