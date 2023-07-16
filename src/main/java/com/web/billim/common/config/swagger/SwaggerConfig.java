package com.web.billim.common.config.swagger;

import com.fasterxml.classmate.TypeResolver;
import com.web.billim.security.dto.LoginRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

    private final TypeResolver typeResolver;

    public SwaggerConfig(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    @Bean
    public Docket swaggerApi(){

        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(mySwaggerInfo())
                .additionalModels(typeResolver.resolve(LoginRequest.class));
    }

    private ApiInfo mySwaggerInfo() {
        return new ApiInfoBuilder()
                .title("BILLIM")
                .description("server api")
                .build();
    }

}
