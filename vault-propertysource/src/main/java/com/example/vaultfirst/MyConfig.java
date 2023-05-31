package com.example.vaultfirst;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.vault.annotation.VaultPropertySource;
import org.springframework.vault.core.VaultTemplate;

@Configuration
@VaultPropertySource("secret/gs-vault-config")
public class MyConfig {

  @Autowired
  Environment env;

  @Bean
  public TestBean testBean() {
    TestBean testBean = new TestBean();
    testBean.setUser(env.getProperty("example.username"));
    testBean.setPassword(env.getProperty("example.password"));
    return testBean;
  }
}
