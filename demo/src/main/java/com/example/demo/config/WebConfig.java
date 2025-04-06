package com.example.demo.config;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Normalize the path to ensure it ends with a slash
        String normalizedUploadDir = uploadDir.endsWith("/") ? uploadDir : uploadDir + "/";

        // Create the directory if it doesn’t exist
        File uploadDirectory = new File(normalizedUploadDir);
        if (!uploadDirectory.exists()) {
            try {
                Files.createDirectories(Paths.get(normalizedUploadDir));
                System.out.println("Created upload directory: " + uploadDirectory.getAbsolutePath());
            } catch (Exception e) {
                System.err.println("Failed to create upload directory: " + e.getMessage());
            }
        }

        // Log the absolute path for debugging
        String filePath = normalizedUploadDir + "ef0c7bc0-0da5-4321-80e8-e1ffbaf7a62b_Picture1.png";
        System.out.println("File Path: " + new File(filePath).getAbsolutePath());
        
        // Register the dynamic resource handler
        System.out.println("Registering resource handler for /uploads/files/** with location: file:" + normalizedUploadDir);
        registry.addResourceHandler("/uploads/files/**")
                .addResourceLocations("file:" + normalizedUploadDir);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}