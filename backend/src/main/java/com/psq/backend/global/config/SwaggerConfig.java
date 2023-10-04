package com.psq.backend.global.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 접속 주소 : http://localhost:8080/swagger-ui/index.html
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI API() {
        Info info = new Info()
                .title("Problem Sovling Queue")
                .version("V1")
                .description("PSQ API");

        return new OpenAPI().info(info);
    }
}