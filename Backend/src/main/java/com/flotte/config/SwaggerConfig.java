package com.flotte.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Gestion de Flotte de Véhicules - API")
                .version("1.0.0")
                .description("API REST pour la gestion de la flotte de véhicules : " +
                    "véhicules, chauffeurs, missions, consommation et reporting."));
              
    }
}