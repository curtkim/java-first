package com.example.s3first;

import com.amazonaws.util.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@SpringBootTest
@Testcontainers
public class BasicSyncTest {

  private static final DockerImageName LOCALSTACK_NAME = DockerImageName.parse("localstack/localstack");

  @Container
  public static LocalStackContainer localStackContainer = new LocalStackContainer(LOCALSTACK_NAME)
      .withServices(S3);


  @TestConfiguration
  static class TestInnerConfiguration {
    @Bean
    public S3Client s3Client() {
      return S3Client.builder()
          .endpointOverride(localStackContainer.getEndpointOverride(S3))
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
  S3Client s3Client;


  @Test
  public void createBucket(){
    String bucket = "test-bucket";
    CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
        .bucket(bucket)
        .build();
    s3Client.createBucket(bucketRequest);

    assertEquals(Arrays.asList(bucket), s3Client.listBuckets().buckets().stream().map(b -> b.name()).collect(Collectors.toList()));
  }

  @Test
  public void putObjectAndGetObject() throws IOException {
    String bucket = "test-bucket";
    String filename = "temp.txt";
    String body = "Hello World!";

    CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
        .bucket(bucket)
        .build();
    s3Client.createBucket(bucketRequest);


    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(filename)
        .build();
    s3Client.putObject(putObjectRequest, RequestBody.fromBytes(body.getBytes()));

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(filename)
        .build();
    ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObjectAsBytes(getObjectRequest);
    String result = new String(responseBytes.asByteArray());
    assertEquals(body, result);


    ResponseInputStream<GetObjectResponse> resInputStream = s3Client.getObject(getObjectRequest);
    assertEquals(body, IOUtils.toString(resInputStream));
  }


}
