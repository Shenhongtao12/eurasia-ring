package com.sht.eurasiaring.config;

import com.sht.eurasiaring.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
//public class InterceptorConfig extends WebMvcConfigurationSupport {
//
//    @Autowired
//    private JwtInterceptor jwtInterceptor;
//
//    /**
//     * 添加拦截器的配置
//     */
//    @Override
//    protected void addInterceptors(InterceptorRegistry registry) {
//        //1.添加自定义拦截器
//        registry.addInterceptor(jwtInterceptor).
//                addPathPatterns("/ring/*/**").//2.指定拦截器的url地址
//                excludePathPatterns("/ring/user/login","/ring/user/init","/eurasia/**");//3.指定不拦截的url地址
//    }
//}

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/ring/*/**")
                .excludePathPatterns("/ring/user/login","/ring/user/init","/ring/user/getToken");

        WebMvcConfigurer.super.addInterceptors(registry);
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
                .allowCredentials(true).maxAge(3600);
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/eurasia/**").addResourceLocations("file:/eurasia/");
    }
}
