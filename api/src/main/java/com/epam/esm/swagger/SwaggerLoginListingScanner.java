package com.epam.esm.swagger;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Plugin for documenting {@code /login} URL endpoint exposed by Spring security filter
 */
@Component
public class SwaggerLoginListingScanner implements ApiListingScannerPlugin {

  private final CachingOperationNameGenerator operationNames;

  /**
   * @param operationNames - CachingOperationNameGenerator is a component bean that is available to
   *     be autowired
   */
  public SwaggerLoginListingScanner(CachingOperationNameGenerator operationNames) { // <9>
    this.operationNames = operationNames;
  }

  @Override
  public List<ApiDescription> apply(DocumentationContext context) {
    return Collections.singletonList(
        new ApiDescription(
            null,
            "/gift-rest-service/login",
            "Login to acquire JWT token",
            "Sign in with email and password to receive authentication token",
            Collections.singletonList(
                new OperationBuilder(operationNames)
                    .summary("Sign in with credentials")
                    .tags(Collections.singleton("JWT Authentication"))
                    .authorizations(new ArrayList<>())
                    .position(1)
                    .codegenMethodNameStem("loginPost")
                    .method(HttpMethod.POST)
                    .notes("Checks credentials and if success issues authorization token")
                    .requestParameters(
                        Collections.singletonList(
                            new RequestParameterBuilder()
                                .description("User email and password")
                                .name("UserLoginDTO")
                                .in(ParameterType.BODY)
                                .required(true)
                                .query(q -> q.model(m -> m.name("UserLoginDTO").scalarModel(ScalarType.OBJECT)))
                                .build()))
                    .responses(responseMessages())
                    .build()),
            false));
  }

  /** @return Set of response messages that overide the default/global response messages */
  private Set<Response> responseMessages() {
    HashSet<Response> responseMessages = new HashSet<>();
    responseMessages.add(
        new ResponseBuilder()
            .code("200")
            .description("JWT Authentication token")
            .representation(MediaType.APPLICATION_JSON)
            .apply(r -> r.model(m -> m.scalarModel(ScalarType.STRING)))
            .build());
    responseMessages.add(new ResponseBuilder().code("401").description("Bad credentials").build());
    responseMessages.add(
        new ResponseBuilder().code("500").description("Internal server error").build());
    return responseMessages;
  }

  @Override
  public boolean supports(@NonNull DocumentationType delimiter) {
    return DocumentationType.SWAGGER_2.equals(delimiter);
  }
}
