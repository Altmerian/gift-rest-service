package com.epam.esm.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication(scanBasePackages = "com.epam.esm")
public class AppConfig extends WebSecurityConfigurerAdapter {

  @Override
  public void configure(WebSecurity webSecurity) throws Exception {
    webSecurity.ignoring().antMatchers("/**");
  }

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
