package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    String accessKey = "";
    String secretAccessKey = "";
    String endpoint = "";


    //@Bean
    public S3Client s3Client(){
      return null;
      /*
      AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(endpoint, "");

      AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretAccessKey);
      AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);

      return AmazonS3ClientBuilder.standard()
          .withEndpointConfiguration(endpointConfiguration)
          .withCredentials(credentialsProvider)
          .build();
      */
    }
}
