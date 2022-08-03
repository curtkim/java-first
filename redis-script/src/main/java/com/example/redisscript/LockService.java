package com.example.redisscript;

import org.springframework.beans.factory.annotation.Autowired;
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
public class LockService {

  @Autowired
  private RedisScript<Boolean> script;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  public Boolean acquire(String id){
    return this.redisTemplate.execute(script, Arrays.asList(id), "READY", "WORKING");
  }

  public List<Object> doit(String id, String value){
    return this.redisTemplate.execute(new SessionCallback<List<Object>>() {
      @Override
      public List<Object> execute(RedisOperations operations) throws DataAccessException {
        operations.multi();
        Boolean result = (Boolean)operations.execute(script, Arrays.asList(id), "READY", "WORKING");
        System.out.println(result);
        operations.opsForValue().set(id, value);
        return operations.exec();
      }
    });
  }
}
