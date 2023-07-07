package com.example.domain;

import com.example.lettuce.data.LettuceTemplate;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class SliceDao {

  LettuceTemplate lettuceTemplate;

  public void add(String key, List<String> values){
    lettuceTemplate.execute(key, (commands)->{
      commands.rpush(key, values.toArray(new String[]{}));
      return null;
    });
  }

  public List<String> get(String slicesKey) {
    return (List<String>)lettuceTemplate.execute(slicesKey, (commands)->{
      return commands.lrange(slicesKey, 0, -1);
    });
  }
}
