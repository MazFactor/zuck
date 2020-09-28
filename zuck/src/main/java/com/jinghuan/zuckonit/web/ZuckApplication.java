package com.jinghuan.zuckonit.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan(value = "com.jinghuan.zuck.*")
public class ZuckApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuckApplication.class, args);
	}

}
