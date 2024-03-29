package com.example.vaultrepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.vault.repository.mapping.Secret;

import java.util.List;

@NoArgsConstructor // 꼭 필요함.
@AllArgsConstructor
@ToString
@Secret(value = "myteam/apps")
public class App {

  @Id
  String id;

  String name;

  List<String> appkeys;

  MethodType methodType;

  int ttlDays;

  AppInfo info;

}
