package com.mercadolibre.ipinfo.config.interceptor;

import com.mercadolibre.ipinfo.interceptor.IpDenyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private IpDenyInterceptor ipDenyInterceptor;

    public InterceptorConfig(IpDenyInterceptor ipDenyInterceptor) {
        this.ipDenyInterceptor = ipDenyInterceptor;
    }

    /**
     * Registracion de interceptor para distintas URLs, en este caso se configura un interceptor para el unico
     * endpoint que disponibiliza la API
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipDenyInterceptor).addPathPatterns("/v1/api/{ip}/info");
    }
}
