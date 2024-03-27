package com.example.myshopdaknong.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cấu hình đường dẫn tới tài nguyên tĩnh và lưu trữ
        registry.addResourceHandler("/public/images/**")
                .addResourceLocations("classpath:/public/images/");

        // Cấu hình bỏ qua các đường dẫn tài nguyên khi kiểm tra đăng nhập
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        // Thêm ResourceHandler mới cho /cart/images/
        registry.addResourceHandler("/cart/images/**")
                .addResourceLocations("classpath:/cart/images/");

    }
}
