package com.example.vaultfirst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import java.util.Map;

@SpringBootApplication
public class VaultFirstApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(VaultFirstApplication.class, args).close();
  }

  @Autowired
  private VaultTemplate vaultTemplate;

  @Autowired
  private TestBean testBean;

  @Override
  public void run(String... args) {
    VaultResponse response = vaultTemplate.read("secret/data/gs-vault-config");
    Map<String, String> map = (Map<String, String>)response.getData().get("data");
    System.out.println(map.get("example.username"));
    System.out.println(map.get("example.password"));


    System.out.println(testBean);
  }
}
