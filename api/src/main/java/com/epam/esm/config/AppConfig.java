package com.epam.esm.config;

import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import java.time.format.DateTimeFormatter;

@SpringBootApplication(scanBasePackages = "com.epam.esm")
public class AppConfig {

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    String dateTimeFormat = "yyyy-MM-dd hh:mm:ss XXX";
    return builder -> {
      builder.simpleDateFormat(dateTimeFormat);
      builder.serializers(new ZonedDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
    };
  }

  public static void main(String[] args) {
    SpringApplication.run(AppConfig.class, args);
  }
}
