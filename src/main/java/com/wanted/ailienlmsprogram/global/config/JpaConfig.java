package com.wanted.ailienlmsprogram.global.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.wanted.ailienlmsprogram")
@EntityScan(basePackages = "com.wanted.ailienlmsprogram")
public class JpaConfig {
}
