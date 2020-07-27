package com.epam.esm.swagger;

import com.epam.esm.dto.UserLoginDTO;
import com.fasterxml.classmate.TypeResolver;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
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

@Component
public class SwaggerLoginListingScanner implements ApiListingScannerPlugin {

  // tag::api-listing-plugin[]
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
            "sign in with email and password to receive authentication token",
            "sign in with email and password to receive authentication token",
            Collections.singletonList(
                new OperationBuilder(operationNames)
                    .summary("sign in with credentials")
                    .tags(Collections.singleton("jwt-authentication-filter"))
                    .authorizations(new ArrayList<>())
                    .position(1)
                    .codegenMethodNameStem("loginPost")
                    .method(HttpMethod.POST)
                    .notes("Checks credentials and if success issues authorization token")
                    .parameters(
                        Collections.singletonList(
                            new ParameterBuilder()
                                .description("User credentials")
                                .type(new TypeResolver().resolve(UserLoginDTO.class))
                                .name("userLogin")
                                .parameterType("body")
                                .parameterAccess("access")
                                .required(true)
                                .modelRef(new ModelRef("UserLoginDTO"))
                                .build()))
                    .responses(responseMessages())
                    .responseModel(new ModelRef(("UserToken")))
                    .build()),
            false));
  }

  /** @return Set of response messages that overide the default/global response messages */
  private Set<Response> responseMessages() {
    HashSet<Response> responseMessages = new HashSet<>();
    responseMessages.add(new ResponseBuilder().code("200").description("UserToken").build());
    responseMessages.add(new ResponseBuilder().code("401").description("Bad credentials").build());
    responseMessages.add(
        new ResponseBuilder().code("500").description("Internal server error").build());
    return responseMessages;
  }

  // tag::api-listing-plugin[]

  @Override
  public boolean supports(@NonNull DocumentationType delimiter) {
    return DocumentationType.SWAGGER_2.equals(delimiter);
  }
}
