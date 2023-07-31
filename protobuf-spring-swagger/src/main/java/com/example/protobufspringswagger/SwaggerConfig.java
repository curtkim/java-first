package com.example.protobufspringswagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;
import com.innogames.springfox_protobuf.ProtobufPropertiesModule;
import io.swagger.v3.core.jackson.ModelResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  public ObjectMapper objectMapper() {
    final ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new ProtobufPropertiesModule());
    objectMapper.registerModule(new ProtobufModule());
    return objectMapper;
  }

  @Bean
  public ModelResolver modelResolver() {
    return new ModelResolver(objectMapper());
  }
}
