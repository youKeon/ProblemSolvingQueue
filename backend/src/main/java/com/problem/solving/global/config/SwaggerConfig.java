package com.problem.solving.global.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
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


        SecurityScheme auth = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.COOKIE).name("JSESSIONID");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("AUTH");

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("AUTH", auth))
                .addSecurityItem(securityRequirement)
                .info(info);
    }
}