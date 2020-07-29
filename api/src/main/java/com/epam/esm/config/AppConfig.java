package com.epam.esm.config;

import com.epam.esm.dto.UserLoginDTO;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Lists;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Spring boot auto configuration and swagger documentation setup
 */
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
  public Docket api(TypeResolver typeResolver) {
    final List<Response> globalResponses = Arrays.asList(
        new ResponseBuilder()
            .code("400")
            .description("Bad Request")
            .build(),
        new ResponseBuilder()
            .code("403")
            .description("Forbidden")
            .build(),
        new ResponseBuilder()
            .code("500")
            .description("Internal server error")
            .build());

    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.epam.esm"))
        .paths(PathSelectors.any())
        .build()
        .additionalModels(typeResolver.resolve(UserLoginDTO.class))
        .useDefaultResponseMessages(false)
        .globalResponses(HttpMethod.GET, globalResponses)
        .globalResponses(HttpMethod.POST, globalResponses)
        .globalResponses(HttpMethod.DELETE, globalResponses)
        .globalResponses(HttpMethod.PUT, globalResponses)
        .globalResponses(HttpMethod.PATCH, globalResponses)
        .apiInfo(
            new ApiInfo(
                "Gift rest service API",
                "API for CRUD operations with gift certificates, tags and the ability to order them by users",
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
        .consumes(DEFAULT_PRODUCES_AND_CONSUMES)
        .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
        .directModelSubstitute(java.time.LocalDateTime.class, Date.class)
        .securityContexts(Lists.newArrayList(securityContext()))
        .securitySchemes(Lists.newArrayList(apiKey()));
  }

  private ApiKey apiKey() {
    return new ApiKey("Bearer", HttpHeaders.AUTHORIZATION, "header");
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder()
        .securityReferences(defaultAuth())
        .operationSelector(o -> o.getName().contains("Order") || o.getName().contains("User"))
        .build();
  }

  List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope
        = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return Lists.newArrayList(
        new SecurityReference("Bearer", authorizationScopes));
  }
}
