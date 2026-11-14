package com.ssg.telcopulse.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@Configuration 
@EnableJpaAuditing    //Enables JPA Auditing feature across the project
public class JpaAuditingConfig {

}
