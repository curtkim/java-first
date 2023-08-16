package com.example.vaultrepository;

import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.vault.repository.mapping.Secret;

import java.util.List;

@AllArgsConstructor
@Secret(value = "naviplatform/mapmatch_apps")
public class App {

  @Id
  String id;

  String name;

  List<String> appkeys;

}
