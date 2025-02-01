package com.backend.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestTemplate;

@Configuration
@Profile("dev")
@EnableRetry
public class DevConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
