package com.yuyin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yuyin.htmlToPdf.dao")
public class YuyintestApplication {

	public static void main(String[] args) {
		SpringApplication.run(YuyintestApplication.class, args);
	}
}
