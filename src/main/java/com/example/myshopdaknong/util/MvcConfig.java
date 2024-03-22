package com.example.myshopdaknong.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String dirName="public/images";
        Path userDir= Paths.get(dirName);
        String userPhotoPath=userDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/"+dirName+"/**")
                .addResourceLocations("file:/"+userPhotoPath+"/");
    }
}
