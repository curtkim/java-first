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

  public String getUsername() {
    return env.getProperty("example.username");
  }

  @Value("${example.password}")
  String password;


  public static void main(String[] args) {
    SpringApplication.run(VaultCloudFirstApplication.class, args);
  }

  @Override
  public void run(String... args) {

    Logger logger = LoggerFactory.getLogger(VaultCloudFirstApplication.class);

    logger.info("----------------------------------------");
    logger.info("Configuration properties");
    logger.info("   example.username is {}", getUsername());
    logger.info("   example.password is {}", password);
    logger.info("----------------------------------------");
  }
}
