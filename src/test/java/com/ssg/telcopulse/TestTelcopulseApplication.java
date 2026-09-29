package com.ssg.telcopulse;

import org.springframework.boot.SpringApplication;

public class TestTelcopulseApplication {

	public static void main(String[] args) {
		SpringApplication.from(TelcopulseApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
