package com.web.billim.common.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("BILLIM")
                .version("v0.0.1")
                .description("BILLIM API Document");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
