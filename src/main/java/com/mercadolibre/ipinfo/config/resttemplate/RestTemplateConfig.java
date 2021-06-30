package com.mercadolibre.ipinfo.config.resttemplate;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Esta clase se encarga de crear un rest template generico e insertalo en el contexto de spring
 * Adicionalmente se podria configurar el RestTemplate para que funcione con un pool de conexiones, de esa forma no
 * crearia conexiones cada vez que se desee usar.
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .build();
    }
}
