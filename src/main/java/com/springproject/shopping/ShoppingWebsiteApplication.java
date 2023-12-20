package com.springproject.shopping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ShoppingWebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingWebsiteApplication.class, args);
	}

}
