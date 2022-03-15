package com.ajclaros.payouts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class PayoutsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PayoutsApplication.class, args);
	}

}
