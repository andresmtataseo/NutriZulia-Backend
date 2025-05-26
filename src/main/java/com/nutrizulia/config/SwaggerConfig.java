package com.nutrizulia.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Nutrizulia")
                        .version("1.0")
                        .description("Documentación de la API de Nutrizulia")
                        .contact(new Contact()
                                .name("Andrés Moreno")
                                .email("andres@nutrizulia.com")
                                .url("https://nutrizulia.com")));
    }
}
