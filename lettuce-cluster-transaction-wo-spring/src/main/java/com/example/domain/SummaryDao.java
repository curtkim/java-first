package com.example.domain;

import com.example.lettuce.data.LettuceTemplate;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SummaryDao {

  LettuceTemplate<String> lettuceTemplate;

  public void watch(String key){
    lettuceTemplate.execute(key, (commands)->{
      commands.watch(key);
      return null;
    });
  }

  public void set(String key, String value){
    lettuceTemplate.execute(key, (commands)->{
      commands.set(key, value);
      return null;
    });
  }

  public String get(String summaryKey) {
    return lettuceTemplate.execute(summaryKey, (commands)->{
      return commands.get(summaryKey);
    });
  }
}
