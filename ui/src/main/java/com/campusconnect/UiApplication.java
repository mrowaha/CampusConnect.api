package com.campusconnect;

import com.campusconnect.image.BucketValidationRunner;
import com.campusconnect.ui.config.RoledJwtProperties;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
@SpringBootApplication
public class UiApplication {

	private final BucketValidationRunner bucketValidationRunner;


	public static void main(String[] args) {
		SpringApplication.run(UiApplication.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner(RoledJwtProperties roledJwt) {
		return args -> {System.out.println(roledJwt);
	};
	}

}
