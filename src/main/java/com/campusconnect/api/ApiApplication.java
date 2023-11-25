package com.campusconnect.api;

import com.campusconnect.api.config.RoledJwtProperties;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner(RoledJwtProperties roledjwt) {
		return args -> {System.out.println(roledjwt);};
	}

}
