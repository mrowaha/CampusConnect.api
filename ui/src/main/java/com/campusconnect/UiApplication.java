package com.campusconnect;

import com.campusconnect.image.BucketValidationRunner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@RequiredArgsConstructor
@SpringBootApplication
public class UiApplication {

	private final BucketValidationRunner bucketValidationRunner;

	public static void main(String[] args) {
		SpringApplication.run(UiApplication.class, args);
	}

}
