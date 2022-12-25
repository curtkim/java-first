package com.example.rediskryo;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Import({EmbededRedisConfig.class, RedisConfig.class})
@DataRedisTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class MyRedisTemplateTest {

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  @Test
  public void t1_set(){
    redisTemplate.opsForValue().set("a", new Person("kim", 40));
  }

  @Test
  public void t2_get(){
    System.out.println(redisTemplate.opsForValue().get("a"));
    assertEquals(new Person("kim", 40), redisTemplate.opsForValue().get("a"));
  }
}
