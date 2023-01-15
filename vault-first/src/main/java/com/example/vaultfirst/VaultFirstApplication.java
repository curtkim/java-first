package com.example.vaultfirst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(MyConfiguration.class)
public class VaultFirstApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(VaultFirstApplication.class, args);
  }


  private final MyConfiguration configuration;

  public VaultFirstApplication(MyConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public void run(String... args) {

    Logger logger = LoggerFactory.getLogger(VaultFirstApplication.class);

    logger.info("----------------------------------------");
    logger.info("Configuration properties");
    logger.info("   example.username is {}", configuration.getUsername());
    logger.info("   example.password is {}", configuration.getPassword());
    logger.info("----------------------------------------");
  }
}
