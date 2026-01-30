package com.newsgroupmanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Get the parent folder path
        String parentPath = new File(System.getProperty("user.dir")).getParent();

        // Tell Spring to serve files from this folder at /uploads/images/profile/**
        registry.addResourceHandler("/uploads/images/profile/**")
                .addResourceLocations("file:" + parentPath + File.separator + "news-group-management-uploads" + File.separator + "images" + File.separator + "profile" + File.separator);

        // Tell Spring to serve files from this folder at /uploads/images/profile/**
        registry.addResourceHandler("/uploads/images/post/**")
                .addResourceLocations("file:" + parentPath + File.separator + "news-group-management-uploads" + File.separator + "images" + File.separator + "post" + File.separator);
    }
}
