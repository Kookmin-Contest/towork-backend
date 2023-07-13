package com.backend.towork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ToworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToworkApplication.class, args);
	}

}
