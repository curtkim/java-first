package com.example.protobufspring;


import com.baeldung.protobuf.BaeldungTraining.Course;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;


@SpringBootTest
class ProtobufSpringApplicationTests {

  @Autowired
  private ProtobufHttpMessageConverter protobufHttpMessageConverter;

  //@LocalServerPort
  private int port = 8080;

  @Test
  public void whenUsingRestTemplate_thenSucceed() {
    RestTemplate restTemplate = new RestTemplate(Arrays.asList(protobufHttpMessageConverter));
    ResponseEntity<Course> course = restTemplate.getForEntity(getUrl(), Course.class);
    assertResponse(course.toString());
  }

  /*
  @Test
  public void whenUsingHttpClient_thenSucceed() throws IOException {
    InputStream responseStream = executeHttpRequest(getUrl());
    String jsonOutput = convertProtobufMessageStreamToJsonString(responseStream);
    assertResponse(jsonOutput);
  }

  private InputStream executeHttpRequest(String url) throws IOException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpGet request = new HttpGet(url);
    HttpResponse httpResponse = httpClient.execute(request);
    return httpResponse.getEntity().getContent();
  }

  private String convertProtobufMessageStreamToJsonString(InputStream protobufStream) throws IOException {
    JsonFormat jsonFormat = new JsonFormat();
    Course course = Course.parseFrom(protobufStream);
    return jsonFormat.printToString(course);
  }
  */

  private void assertResponse(String response) {
    assertThat(response, containsString("id"));
    assertThat(response, containsString("course_name"));
    assertThat(response, containsString("REST with Spring"));
    assertThat(response, containsString("student"));
    assertThat(response, containsString("first_name"));
    assertThat(response, containsString("last_name"));
    assertThat(response, containsString("email"));
    assertThat(response, containsString("john.doe@baeldung.com"));
    assertThat(response, containsString("richard.roe@baeldung.com"));
    assertThat(response, containsString("jane.doe@baeldung.com"));
    assertThat(response, containsString("phone"));
    assertThat(response, containsString("number"));
    assertThat(response, containsString("type"));
  }

  private String getUrl() {
    return "http://localhost:" + port + "/courses/1";
  }
}
