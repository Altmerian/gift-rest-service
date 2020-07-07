package com.epam.esm.config;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan("com.epam.esm")
public class ServiceConfig {

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    //Certificate mapping
    modelMapper
        .typeMap(CertificateDTO.class, Certificate.class)
        .addMappings(mapping -> mapping.skip(Certificate::setId))
        .addMappings(mapping -> mapping.skip(Certificate::setCreationDate))
        .addMappings(mapping -> mapping.skip(Certificate::setModificationDate));
    //User mapping
    modelMapper
        .typeMap(UserDTO.class, User.class)
        .addMappings(mapping -> mapping.skip(User::setId));
    //Order mapping
    modelMapper
        .typeMap(OrderDTO.class, Order.class)
        .addMappings(mapping -> mapping.skip(Order::setId))
        .addMappings(mapping -> mapping.skip(Order::setCreationDate))
        .addMappings(mapping -> mapping.skip(Order::setCost));
    return modelMapper;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
