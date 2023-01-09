package com.example.boardservice;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/board")
public class BoardController {

  @GetMapping("/")
  public List<BoardItem> findItem() {
    return Arrays.asList(new BoardItem(1, "hi", "nice to meet you"));
  }
}

