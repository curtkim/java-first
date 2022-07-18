package com.example.redistransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;


public class MyDao2 {

  @Autowired
  private StringRedisTemplate redisTemplate;

  public void doit2(){

    redisTemplate.execute(new SessionCallback() {
      @Override
      public Object execute(RedisOperations operations) throws DataAccessException {
        operations.opsForValue().set("KAROL", "2");
        throw new RuntimeException("exception occur");
      }
    });
  }
}
