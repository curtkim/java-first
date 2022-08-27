package com.example.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Arrays;

@SpringBootApplication
public class JacksonApplication {

  public static void main(String[] args) {
    SpringApplication.run(JacksonApplication.class, args);
  }

  @Bean
  public HttpMessageConverter<Object> createJson2HttpMessageConverter() {
    ObjectMapper mapper =new ObjectMapper();
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setObjectMapper(mapper);
    return converter;
  }

  @Bean
  public HttpMessageConverter<Object> createTaxiJson2HttpMessageConverter() {
    ObjectMapper mapper =new ObjectMapper();
    SimpleModule module = new SimpleModule("CustomCarSerializer", new Version(1, 0, 0, null, null, null));
    module.addSerializer(Foo.class, new FooSerializer());
    mapper.registerModule(module);

    MediaType taxiJson = new MediaType("application", "taxi-json");

    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setSupportedMediaTypes(Arrays.asList(taxiJson));
    converter.setObjectMapper(mapper);
    return converter;
  }

}
