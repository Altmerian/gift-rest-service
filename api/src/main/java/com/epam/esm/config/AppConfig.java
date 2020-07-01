package com.epam.esm.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.epam.esm")
public class AppConfig {

//  @Bean
//  public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
//    String dateTimeFormat = "yyyy-MM-dd hh:mm:ss XXX";
//    return builder -> {
//      builder.simpleDateFormat(dateTimeFormat);
//      builder.serializers(new ZonedDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
//    };
//  }

  public static void main(String[] args) {
    SpringApplication.run(AppConfig.class, args);
  }
}
