package com.example.secureboardexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SecureboardexampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecureboardexampleApplication.class, args);
    }

}
