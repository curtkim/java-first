package com.example.vaultrepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.vault.repository.mapping.Secret;

import java.time.Duration;
import java.util.List;

@NoArgsConstructor // 꼭 필요함.
@AllArgsConstructor
@ToString
@Secret(value = "naviplatform/mapmatch_apps")
public class App {

  @Id
  String id;

  String name;

  List<String> appkeys;

  int ttlDays;

}
