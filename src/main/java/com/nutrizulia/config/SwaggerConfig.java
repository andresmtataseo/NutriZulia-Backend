package com.nutrizulia.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "API de Nutrizulia", version = "1.0", description = "Documentación de la API para la aplicación Nutrizulia"),
        servers = {
                @Server(url = "http://localhost:8080", description = "Servidor Local")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Autenticación JWT. Introduce tu token JWT aquí (sin el prefijo 'Bearer ')."
)
public class SwaggerConfig {

}
