package com.web.billim.swagger;

import com.fasterxml.classmate.TypeResolver;
import com.web.billim.security.domain.LoginRequest;
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
                .apis(RequestHandlerSelectors.any()) // 프로젝트 내의 모든 패키지에 적용
//                .apis(RequestHandlerSelectors.basePackage("")) 특정 패키지 내의 모든 Controller에 적용
                .paths(PathSelectors.any())
//                조건에 맞는 API 필터링
//                PathSelectors.any() 모든 Api 통과
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
