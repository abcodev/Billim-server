package com.web.billim.common.config.swagger;

import com.fasterxml.classmate.TypeResolver;
import com.web.billim.security.dto.LoginRequest;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;

//@Configuration
//@EnableWebMvc
//public class SwaggerConfig {
//
//    private final TypeResolver typeResolver;
//
//    public SwaggerConfig(TypeResolver typeResolver) {
//        this.typeResolver = typeResolver;
//    }
//
//    @Bean
//    public Docket swaggerApi(){
//
//        return new Docket(DocumentationType.OAS_30)
//                .select()
//                .apis(RequestHandlerSelectors.any())
////                .paths(PathSelectors.any())
//                .apis(RequestHandlerSelectors.basePackage("com.web.billim"))
//                .build()
//                .apiInfo(mySwaggerInfo())
//                .additionalModels(typeResolver.resolve(LoginRequest.class));
//    }
//
//    private ApiInfo mySwaggerInfo() {
//        return new ApiInfoBuilder()
//                .title("BILLIM")
//                .description("BILLIM Server Api")
//                .build();
//    }
//
//}

//@Configuration
//public class SwaggerConfig {
//    @Bean
//    public OpenAPI openAPI() {
//        return new OpenAPI()
//                .info(new Info().title("BILLIM API")    // API 제목
//                        .description("BILLIM application")  // API 설명
//                        .version("v0.0.1"));	//  API 버전
//    }
//}

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("BILLIM API Document")
                .version("v0.0.1")
                .description("BILLIM API 명세서입니다.");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
