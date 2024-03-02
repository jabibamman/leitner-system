package com.esgi.leitnersystem.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Leitner system", version = "1.0.0",
        description =
            "This API aim to provide feature to manage a graphical interface for Leitner System.\n")
    )
public class SpringDocConfig {
  @Value("${server.url}") private String serverUrl;

  @Value("${server.description}") private String serverDescription;

  @Value("${springdoc.swagger-group}") private String swaggerGroup;

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group(swaggerGroup)
        .pathsToMatch("/**")
        .build();
  }

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI().addServersItem(
        new Server().url(serverUrl).description(serverDescription));
  }
}
