package com.estelair.iavatarstudioback.configuration;

import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AdditionalResourceWebConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/spring-imagenes/**")
                .addResourceLocations("file:///C:/Users/user/Desktop/spring-imagenes/");
    }
}
