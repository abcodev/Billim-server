package com.web.billim.common.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Billim")
                .version("v0.0.1")
                .description("Billim API Document");
        return new OpenAPI()
                .components(new Components())
                .paths(customPaths())
                .info(info);
    }

    private Paths customPaths() {
        Paths paths = new Paths();
        paths.addPathItem("/auth/login", customLoginPath());
        return paths;
    }

    private PathItem customLoginPath() {
        Schema schema = new Schema()
                .type("object")
                .addProperty("email", new Schema().type("string"))
                .addProperty("password", new Schema().type("string"));

        Operation operation = new Operation()
                .summary("회원 로그인")
                .description("로그인 인증 후 accessToken 발급")
                .requestBody(new RequestBody()
                        .content(new Content()
                                .addMediaType("application/json", new MediaType()
                                        .schema(schema)))
                )
                .responses(new ApiResponses()
                        .addApiResponse("200", createApiResponse())
                );
        return new PathItem().post(operation);
    }

    private ApiResponse createApiResponse() {
        Schema responseSchema = new Schema()
                .type("object")
                .addProperty("memberId", new Schema().type("long"))
                .addProperty("accessToken", new Schema().type("string"))
                .addProperty("refreshToken", new Schema().type("string"));

        return new ApiResponse()
                .description("Successful response")
                .content(new Content()
                        .addMediaType("application/json", new MediaType()
                                .schema(responseSchema)));
    }
}
