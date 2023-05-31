package com.example.vaultfirst;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class VaultPropertyLoader implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  @Override
  // https://ankitwasankar.medium.com/load-spring-boot-app-properties-from-database-before-application-start-9af302d5dd54
  public void initialize(ConfigurableApplicationContext applicationContext) {
    //ConfigurableEnvironment env = applicationContext.getEnvironment();
    //new VaultTemplate();
    VaultConfig vaultConfig = new VaultConfig();//applicationContext.getBean(VaultConfig.class);
    VaultTemplate vaultTemplate = new VaultTemplate(vaultConfig.vaultEndpoint(), vaultConfig.clientAuthentication());//vaultConfig.vaultTemplate();//applicationContext.getBean(VaultTemplate.class);

    applicationContext.getEnvironment().getPropertySources().addLast(
        make(vaultTemplate, "secret/data/myteam/db/mydb")
    );
    applicationContext.getEnvironment().getPropertySources().addLast(
        make(vaultTemplate, "secret/data/myteam/s3/servicea")
    );
  }

  MapPropertySource make(VaultTemplate vaultTemplate, String path) {
    String lastPart = last(path.split("/"));
    VaultResponse response = vaultTemplate.read(path);
    Map<String, Object> propertySource = (Map<String, Object>)response.getData().get("data");
    return new MapPropertySource("vault_"+lastPart, prefix(propertySource, lastPart+"."));
  }

  static <T> T last(T[] array) {
    return array[array.length - 1];
  }

  Map<String, Object> prefix(Map<String, Object> map, String prefix) {
    return map.entrySet().stream()
        .collect(Collectors.toMap(e -> prefix + e.getKey(), Map.Entry::getValue));
  }
}
