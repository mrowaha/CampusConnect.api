package com.campusconnect;

import com.campusconnect.ui.config.RoledJwtProperties;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UiApplication {

	public static void main(String[] args) {
		SpringApplication.run(UiApplication.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner(RoledJwtProperties roledJwt) {
		return args -> {System.out.println(roledJwt);};
	}

}
