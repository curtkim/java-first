package com.example.vaultrepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.vault.repository.configuration.EnableVaultRepositories;

@SpringBootApplication
@EnableVaultRepositories
public class VaultRepositoryApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(VaultRepositoryApplication.class, args);
  }

  @Autowired
  AppRepository appRepository;

  @Override
  public void run(String... args) throws Exception {
    appRepository.save(new App("taxi", "taxi앱"));
  }
}
