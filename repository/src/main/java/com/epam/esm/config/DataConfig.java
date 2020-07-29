package com.epam.esm.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.epam.esm")
@EntityScan("com.epam.esm.entity")
public class DataConfig {

}
