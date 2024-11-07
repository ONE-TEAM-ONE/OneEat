package com.sparta.oneeat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class OneeatApplication {

	public static void main(String[] args) {
		SpringApplication.run(OneeatApplication.class, args);
	}

}
