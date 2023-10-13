package com.example.vaultrepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.vault.repository.configuration.EnableVaultRepositories;

import java.time.Duration;
import java.util.Arrays;

@SpringBootApplication
@EnableVaultRepositories
public class VaultRepositoryApplication implements CommandLineRunner {

  public static void main(String[] args) {

    SpringApplicationBuilder builder = new SpringApplicationBuilder()
        .sources(VaultRepositoryApplication.class)
        .bannerMode(Banner.Mode.OFF);

    SpringApplication app = builder.build();
    app.setWebApplicationType(WebApplicationType.NONE);
    app.run(args).close();
    //SpringApplication.run(VaultRepositoryApplication.class, args);
  }

  @Autowired
  AppRepository appRepository;

  @Override
  public void run(String... args) throws Exception {
    appRepository.save(new App("taxi", "taxi앱", Arrays.asList("1", "2"), MethodType.SESSION, 1, new AppInfo("a", 1)));

    Iterable<App> list = appRepository.findAll();
    for(App app : list)
      System.out.println(app);
  }
}
