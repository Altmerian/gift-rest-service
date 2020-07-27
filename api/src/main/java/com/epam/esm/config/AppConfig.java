package com.epam.esm.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication(scanBasePackages = "com.epam.esm")
@ConfigurationPropertiesScan("com.epam.esm")
@EnableSwagger2
public class AppConfig {

  private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES =
      new HashSet<>(Collections.singletonList("application/json"));

  public static void main(String[] args) {
    SpringApplication.run(AppConfig.class, args);
  }

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.epam.esm"))
        .paths(PathSelectors.any())
        .build()
//        .useDefaultResponseMessages(false)
        .globalResponses(HttpMethod.GET,
            Collections.singletonList(new ResponseBuilder()
                .code("500")
                .description("Internal server error")
                .representation(MediaType.APPLICATION_JSON)
                .apply(r ->
                    r.model(m ->
                        m.referenceModel(ref ->
                            ref.key(k ->
                                k.qualifiedModelName(q ->
                                    q.namespace("some:namespace")
                                        .name("ERROR"))))))
                .build()))
        .apiInfo(
            new ApiInfo(
                "Gift rest service API",
                "API for CRUD operations with gift certificates and the ability to order them",
                "1.0",
                "urn:tos",
                new Contact(
                    "Pavel Shakhlovich",
                    "https://github.com/Altmerian/gift-rest-service",
                    "Pavel_Shakhlovich@epam.com"),
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                Collections.emptyList()))
        .produces(DEFAULT_PRODUCES_AND_CONSUMES)
        .consumes(DEFAULT_PRODUCES_AND_CONSUMES);

  }
}
