package com.example.redisscript;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class LockService {

  @Autowired
  private RedisScript<Boolean> script;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  public Boolean acquire(String id){
    return this.redisTemplate.execute(script, Arrays.asList(id), "READY", "WORKING");
  }
}
