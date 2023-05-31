package com.example.vaultcloudfirst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class VaultCloudFirstApplication implements CommandLineRunner {
  @Autowired
  Environment env;

  public static void main(String[] args) {
    SpringApplication.run(VaultCloudFirstApplication.class, args);
  }

  @Override
  public void run(String... args) {
    System.out.println(env.getProperty("mydb.username"));
    System.out.println(env.getProperty("s3.accesskey"));
  }
}
