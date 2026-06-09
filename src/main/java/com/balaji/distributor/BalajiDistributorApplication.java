package com.balaji.distributor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BalajiDistributorApplication {

	public static void main(String[] args) {
		SpringApplication.run(BalajiDistributorApplication.class, args);
	}

}
