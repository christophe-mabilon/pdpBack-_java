package com.b3al.spring.jwt.mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class SpringBootSecurityJwtMongodbApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityJwtMongodbApplication.class, args);
	}
}
