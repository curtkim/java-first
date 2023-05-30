package com.example.s3first;

import com.amazonaws.util.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.buffer.DataBuffer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.core.async.ResponsePublisher;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@SpringBootTest
@Testcontainers
public class BasicAsyncTest {

  private static final DockerImageName LOCALSTACK_NAME = DockerImageName.parse("localstack/localstack");

  @Container
  public static LocalStackContainer localStackContainer = new LocalStackContainer(LOCALSTACK_NAME)
      .withServices(S3);


  @TestConfiguration
  static class TestInnerConfiguration {
    @Bean
    public S3AsyncClient s3AsyncClient() {
      AwsBasicCredentials credentials = AwsBasicCredentials.create(localStackContainer.getAccessKey(), localStackContainer.getSecretKey());
      StaticCredentialsProvider cp = StaticCredentialsProvider.create(credentials);

      SdkAsyncHttpClient httpClient = NettyNioAsyncHttpClient.builder()
          .writeTimeout(Duration.ZERO)
          .build();

      S3Configuration conf = S3Configuration.builder()
          .pathStyleAccessEnabled(true)
          .build();

      S3AsyncClientBuilder b = S3AsyncClient.builder().httpClient(httpClient)
          .region(Region.US_EAST_1)
          .credentialsProvider(cp)
          .serviceConfiguration(conf)
          .endpointOverride(localStackContainer.getEndpointOverride(S3));

      return b.build();
    }
  }

  @Autowired
  S3AsyncClient s3AsyncClient;


  @Test
  public void putObjectAndGetObject() throws IOException, InterruptedException {
    String bucket = "test-bucket";
    String filename = "temp.txt";
    String body = "Hello World!";

    CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
        .bucket(bucket)
        .build();
    CreateBucketResponse createBucketResponse = Mono.fromFuture(s3AsyncClient.createBucket(bucketRequest)).block();


    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(filename)
        .build();
    PutObjectResponse putObjectResponse = Mono.fromFuture(s3AsyncClient.putObject(putObjectRequest, AsyncRequestBody.fromBytes(body.getBytes()))).block();

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(filename)
        .build();
    Mono<ResponsePublisher<GetObjectResponse>> mono = Mono.fromFuture(s3AsyncClient.getObject(getObjectRequest, AsyncResponseTransformer.toPublisher()));
    ResponsePublisher<GetObjectResponse> responsePublisher = mono.block();
    SdkPublisher<ByteBuffer> publisher = responsePublisher;   // NOTE 이 publisher는 netty threadpool에서 동작한다.

    int MAX_BYTES = 100;
    Scheduler mainScheduler = Schedulers.immediate();
    Flux<ByteBuffer> flux = Flux.from(publisher);
    ByteBuffer all = flux
        .publishOn(mainScheduler)     // main thread에 실행되게 한다.
        .scan(ByteBuffer.allocate(MAX_BYTES), (a, b)-> a.put(b))
        .last()
        .block();

    assertEquals(body, new String(all.array(), 0, all.position()));
  }
}
