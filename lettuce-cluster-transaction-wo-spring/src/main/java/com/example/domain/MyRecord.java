package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MyRecord {

  String summary;
  List<String> slices;
}
