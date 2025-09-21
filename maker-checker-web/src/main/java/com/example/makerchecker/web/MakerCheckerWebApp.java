package com.example.makerchecker.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
})
@EnableJpaRepositories(basePackages = "com.example.makerchecker.app.repo")
@EntityScan(basePackages = "com.example.makerchecker.app.entity")
@ComponentScan(basePackages = {
        "com.example.makerchecker.app",
        "com.example.makerchecker.web"
})
@EnableScheduling
public class MakerCheckerWebApp {
    public static void main(String[] args) {
        SpringApplication.run(MakerCheckerWebApp.class, args);
    }
}
