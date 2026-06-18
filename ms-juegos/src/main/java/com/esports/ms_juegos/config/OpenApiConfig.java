package com.esports.ms_juegos.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
        @Bean
        public OpenAPI openAPI() {
                final String securitySchemeName = "bearerAuth";
                return new OpenAPI()
                                .info(new Info()
                                                .title("E-SPORTS - Microservicio de Juegos API")
                                                .version("1.0")
                                                .description("Documentación del microservicio de juegos. "
                                                                + "\n Acá podrás crear los juegos con su género, además de buscarlos y listarlos."))
                                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                                .components(new Components()
                                                .addSecuritySchemes(securitySchemeName,
                                                                new SecurityScheme()
                                                                                .name(securitySchemeName)
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")));
        }
}