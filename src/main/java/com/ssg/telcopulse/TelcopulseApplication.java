package com.ssg.telcopulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing    //Enables JPA Auditing feature across the project
public class TelcopulseApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelcopulseApplication.class, args);
	}

}
