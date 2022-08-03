package com.example.redistransaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.junit.jupiter.api.Assertions.assertNull;

@Import({ServiceConfig.class, RedisTxContextConfig.class})
@DataRedisTest
public class MyServiceTest {

  @Autowired
  MyService myService;


  @Autowired
  private StringRedisTemplate redisTemplate;

  @BeforeEach
  public void beforeEach(){
    redisTemplate.execute((RedisCallback<Object>) connection -> {
      connection.flushDb();
      return null;
    });
  }

  @Test
  public void testDoit(){
    try {
      myService.doit();
    }catch (Exception ex) {
      System.out.println(ex.getMessage());
      //ignore
    }

    String value = redisTemplate.opsForValue().get("SABARADA");
    assertNull(value);
  }
}
