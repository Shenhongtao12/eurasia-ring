package com.sht.eurasiaring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 资源映射路径
 */
@Configuration
public class ImagesConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/eurasia/**").addResourceLocations("file:/eurasia/");
        //registry.addResourceHandler("/deal/**").addResourceLocations("file:F:/deal/");
    }
}
