package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;


@SpringBootTest
@Testcontainers
public class TestS3Upload {
  private static final DockerImageName LOCALSTACK_NAME = DockerImageName.parse("localstack/localstack");

  @Container
  public static LocalStackContainer localStackContainer = new LocalStackContainer(LOCALSTACK_NAME)
      .withServices(S3);


  @TestConfiguration
  public static class WebClientConfiguration {
    @Bean
    public S3Client s3Client() {
      return S3Client.builder()
          .endpointOverride(localStackContainer.getEndpointOverride(LocalStackContainer.Service.S3))
          .credentialsProvider(
              StaticCredentialsProvider.create(
                  AwsBasicCredentials.create(localStackContainer.getAccessKey(), localStackContainer.getSecretKey())
              )
          )
          .region(Region.of(localStackContainer.getRegion()))
          .build();
    }
  }

  @Autowired
  private S3Client s3Client;


  @Test
  @DisplayName("s3 업로드 테스트")
  public void test() throws IOException {
    String bucketName = "mybucket";
    s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());

    String key = "aaaa";
    String content = "bbbb";
    s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(key).build(), RequestBody.fromString(content));

    ResponseInputStream<GetObjectResponse> res = s3Client.getObject(GetObjectRequest.builder().bucket(bucketName).key(key).build());
    String content2 = IOUtils.toString(new InputStreamReader(res));
    assertEquals(content, content2);
  }
}