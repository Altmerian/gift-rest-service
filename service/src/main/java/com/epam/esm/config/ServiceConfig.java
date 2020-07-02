package com.epam.esm.config;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.entity.Certificate;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.epam.esm")
public class ServiceConfig {

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper
        .typeMap(CertificateDTO.class, Certificate.class)
        .addMappings(mapping -> mapping.skip(Certificate::setId))
        .addMappings(mapping -> mapping.skip(Certificate::setCreationDate))
        .addMappings(mapping -> mapping.skip(Certificate::setModificationDate));
    return modelMapper;
  }
}
