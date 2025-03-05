package com.stanok.stanokdelivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StanokDeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(StanokDeliveryApplication.class, args);
	}

}
