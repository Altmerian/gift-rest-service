package com.epam.esm.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.epam.esm")
public class AppConfig {

  public static void main(String[] args) {
    SpringApplication.run(AppConfig.class, args);
  }
}
