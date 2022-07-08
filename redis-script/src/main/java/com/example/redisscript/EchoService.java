package com.example.redisscript;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class EchoService {
  @Autowired
  private RedisScript<String> script;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  public String echo(String msg){
    return this.redisTemplate.execute(script, new ArrayList<>(), msg);
  }
}
