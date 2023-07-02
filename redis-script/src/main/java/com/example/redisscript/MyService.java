package com.example.redisscript;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MyService {

  @Autowired
  @Qualifier("unpack")
  private RedisScript<Boolean> script;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  public boolean doit(String id1, String value1, String id2, List<String> list){
    List<Object> argv = new ArrayList<>();
    argv.add(value1);
    argv.addAll(list);
    return this.redisTemplate.execute(script, Arrays.asList(id1, id2), argv.toArray());
  }
}
