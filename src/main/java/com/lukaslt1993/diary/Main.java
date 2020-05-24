package com.lukaslt1993.diary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.lukaslt1993.diary.*")
@ComponentScan(basePackages = { "com.lukaslt1993.diary*" })
@EntityScan("com.lukaslt1993.diary.*")
@SpringBootApplication
public class Main {
    
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    
}
