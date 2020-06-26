package com.epam.esm.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan("com.epam.esm")
public class AppConfig implements WebMvcConfigurer {

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    MappingJackson2HttpMessageConverter jacksonMessageConverter = new MappingJackson2HttpMessageConverter();
    ObjectMapper objectMapper = jacksonMessageConverter.getObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

//    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
    StdDateFormat stdDateFormat = new StdDateFormat();
    objectMapper.setDateFormat(stdDateFormat);

    objectMapper.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false);
    converters.add(jacksonMessageConverter);
  }

}
