package com.example.boardservice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardItem {
  long id;
  String title;
  String body;
}
