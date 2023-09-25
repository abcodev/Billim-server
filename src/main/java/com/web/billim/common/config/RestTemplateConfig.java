package com.web.billim.common.config;

import org.apache.http.entity.ContentType;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplateBuilder()
			.defaultHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType())
			.build();
	}
}
