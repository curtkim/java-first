package com.example.vaultrepository;

import org.springframework.context.annotation.Configuration;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.config.AbstractVaultConfiguration;
import org.springframework.vault.repository.configuration.EnableVaultRepositories;

@Configuration
public class VaultConfig extends AbstractVaultConfiguration {

  @Override
  public ClientAuthentication clientAuthentication() {
    return new TokenAuthentication("00000000-0000-0000-0000-000000000000");
  }

  @Override
  public VaultEndpoint vaultEndpoint() {
    VaultEndpoint endpoint = VaultEndpoint.create("127.0.0.1", 8200);
    endpoint.setScheme("http");
    endpoint.setPath("v1");
    return endpoint;
  }

}
