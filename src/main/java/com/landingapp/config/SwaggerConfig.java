package com.landingapp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    @Primary
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Информация о API
                .info(new Info()
                        .title("Landing Builder API")
                        .version("1.0.0")
                        .description("REST API для конструктора лендингов с системой аутентификации JWT")
                        .termsOfService("https://example.com/terms")
                        .contact(new Contact()
                                .name("API Support")
                                .email("support@landingapp.com")
                                .url("https://landingapp.com/contact"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))

                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Локальный сервер разработки"),
                        new Server()
                                .url("https://api.landingapp.com")
                                .description("Продакшен сервер")
                ))

                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Введите JWT токен. Получить токен можно через /api/auth/login")
                        )
                        .addSecuritySchemes("cookieAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.COOKIE)
                                        .name("auth_token")
                                        .description("JWT токен из cookies")
                        )
                )

                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .addSecurityItem(new SecurityRequirement().addList("cookieAuth"));
    }

    @Bean
    public OpenAPI customOpenAPIWithTags() {
        return customOpenAPI()
                .addTagsItem(new io.swagger.v3.oas.models.tags.Tag()
                        .name("Authentication")
                        .description("API для аутентификации и управления пользователями"))
                .addTagsItem(new io.swagger.v3.oas.models.tags.Tag()
                        .name("Landings")
                        .description("API для работы с лендингами"))
                .addTagsItem(new io.swagger.v3.oas.models.tags.Tag()
                        .name("Templates")
                        .description("API для работы с шаблонами лендингов"))
                .addTagsItem(new io.swagger.v3.oas.models.tags.Tag()
                        .name("Admin")
                        .description("API для администраторов системы"))
                .addTagsItem(new io.swagger.v3.oas.models.tags.Tag()
                        .name("Public")
                        .description("Публичные API endpoints"));
    }
}
