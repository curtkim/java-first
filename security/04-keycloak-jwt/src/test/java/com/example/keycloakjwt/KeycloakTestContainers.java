package com.example.keycloakjwt;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

import javax.annotation.PostConstruct;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class KeycloakTestContainers {

  private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakTestContainers.class.getName());

  @LocalServerPort
  private int port;

  static final KeycloakContainer keycloak;

  static {
    keycloak = new KeycloakContainer().withRealmImportFile("keycloak/realm-export.json");
    keycloak.start();
  }

  @PostConstruct
  public void init() {
    RestAssured.baseURI = "http://localhost:" + port;
  }

  @DynamicPropertySource
  static void registerResourceServerIssuerProperty(DynamicPropertyRegistry registry) {
    registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> keycloak.getAuthServerUrl() + "/realms/baeldung");
  }

  protected String getJaneDoeBearer() {

    try {
      System.out.println(keycloak.getAuthServerUrl());
      URI authorizationURI = new URIBuilder(keycloak.getAuthServerUrl() + "/realms/baeldung/protocol/openid-connect/token").build();
      WebClient webclient = WebClient.builder()
          .build();

      MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
      formData.put("grant_type", Collections.singletonList("password"));
      formData.put("client_id", Collections.singletonList("baeldung-api"));
      formData.put("username", Collections.singletonList("jane.doe@baeldung.com"));
      formData.put("password", Collections.singletonList("s3cr3t"));

      String result = webclient.post()
          .uri(authorizationURI)
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .body(BodyInserters.fromFormData(formData))
          .retrieve()
          .bodyToMono(String.class)
          .block();

      System.out.println(result);
      //{
      // "access_token":"eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ1RUxRZE1tN0x1b0dYWXFIdEdiMG05ME5BYWJqUE5nWlltRzg0TzBZOG1ZIn0.eyJleHAiOjE2OTE2NTk0OTEsImlhdCI6MTY5MTY1OTE5MSwianRpIjoiOWY3MTJmNmItMjhkMS00NGY3LTlkOTctZDc2MDhhNzIwMWRmIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDozMjc3My9yZWFsbXMvYmFlbGR1bmciLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiZDkwMDE3NGItMzJkZC00NWYwLWJhOTEtOTYyMmZjNGM1MjE3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYmFlbGR1bmctYXBpIiwic2Vzc2lvbl9zdGF0ZSI6IjE5ZGRhMGY3LTg0NjUtNGY3NC1hZGVkLTJlN2Y0YmZjYmFhZSIsInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwic2lkIjoiMTlkZGEwZjctODQ2NS00Zjc0LWFkZWQtMmU3ZjRiZmNiYWFlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiSmFuZSBEb2UiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJqYW5lZG9lIiwiZ2l2ZW5fbmFtZSI6IkphbmUiLCJmYW1pbHlfbmFtZSI6IkRvZSIsImVtYWlsIjoiamFuZS5kb2VAYmFlbGR1bmcuY29tIn0.fgyKbqn6hCBGKzylqHdrElzLCLzoYMsgLehty_CXyQ7svRww1GsMcEleu1P7ai5PKZBXXnnrE3BOXaeoST7zlSH5gTxjE6FipPEZtmvpBWvcLzYnSCjnUtblPGCDP095Pf9cwSohOsf9G--Wxxy_SuDn0To6W99lmeEiOrKIuiyMTgqNMIuV3SpZJXOm5j-Z4rXTSCG5HziRCpK6p9uSGVi277vi0ZJZ8A1_RdidGuPjSmD9Q16xJxMPuvH26GUbvka_vYPTBFlcBmATPek4Vu5DTSLNB7oqLfIP3dFij3m0ynkNI2cPDMuL9r5sjWzcwcnQcU0PoSL042gOUEWiPg","expires_in":300,"refresh_expires_in":1800,"refresh_token":"eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJmN2M3NjEwZi05OGNjLTQ1MGQtODM1YS1lOGQ5ZTFjNDY2NzEifQ.eyJleHAiOjE2OTE2NjA5OTEsImlhdCI6MTY5MTY1OTE5MSwianRpIjoiZGEzZjM5OGEtZmRjNi00MTI5LWFiMWYtOThlY2ZiNDMwNDA5IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDozMjc3My9yZWFsbXMvYmFlbGR1bmciLCJhdWQiOiJodHRwOi8vbG9jYWxob3N0OjMyNzczL3JlYWxtcy9iYWVsZHVuZyIsInN1YiI6ImQ5MDAxNzRiLTMyZGQtNDVmMC1iYTkxLTk2MjJmYzRjNTIxNyIsInR5cCI6IlJlZnJlc2giLCJhenAiOiJiYWVsZHVuZy1hcGkiLCJzZXNzaW9uX3N0YXRlIjoiMTlkZGEwZjctODQ2NS00Zjc0LWFkZWQtMmU3ZjRiZmNiYWFlIiwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwic2lkIjoiMTlkZGEwZjctODQ2NS00Zjc0LWFkZWQtMmU3ZjRiZmNiYWFlIn0.Jw2xhwEF5nGeUhhoGtZOlenXEDIl4ii3n45XkjNkQYk",
      // "token_type":"Bearer",
      // "not-before-policy":0,
      // "session_state":"19dda0f7-8465-4f74-aded-2e7f4bfcbaae",
      // "scope":"email profile"
      // }
      JacksonJsonParser jsonParser = new JacksonJsonParser();

      return "Bearer " + jsonParser.parseMap(result)
          .get("access_token")
          .toString();
    } catch (URISyntaxException e) {
      LOGGER.error("Can't obtain an access token from Keycloak!", e);
    }

    return null;
  }
}
