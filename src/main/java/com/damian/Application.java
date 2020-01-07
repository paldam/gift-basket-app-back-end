package com.damian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.damian.config.*")})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
