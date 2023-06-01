package com.example.vaultfirst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import java.util.Map;

@SpringBootApplication
public class VaultFirstApplication implements CommandLineRunner {

  public static void main(String[] args) {
    new SpringApplicationBuilder()
        .sources(VaultFirstApplication.class)
        .initializers(new VaultPropertyLoader())
        .run(args);
        //.close();
    //configureApplication(new SpringApplicationBuilder()).run(args);
    //SpringApplication.run(VaultFirstApplication.class, args).close();
  }

  @Autowired
  private VaultTemplate vaultTemplate;

  @Autowired
  private Environment env;

  @Override
  public void run(String... args) {
    /*
    VaultResponse response = vaultTemplate.read("secret/data/myteam/db/mydb");
    Map<String, String> map = (Map<String, String>) response.getData().get("data");
    System.out.println(map.get("username"));
    System.out.println(map.get("password"));

    response = vaultTemplate.read("secret/data/myteam/s3/servicea");
    map = (Map<String, String>) response.getData().get("data");
    System.out.println(map.get("accesskey"));
    System.out.println(map.get("secretkey"));
    */


    System.out.println(env.getProperty("mydb.username"));
    System.out.println(env.getProperty("mydb.password"));

    System.out.println(env.getProperty("servicea.accesskey"));
    System.out.println(env.getProperty("servicea.secretkey"));
  }
}
