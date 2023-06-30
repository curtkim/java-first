package com.example.rediscluster;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RedisClusterApplicationTests {

  @Autowired
  private StringRedisTemplate redisTemplate;

  @Test
  public void test() {
    redisTemplate.opsForValue().set("a", "1");
    assertEquals("1", redisTemplate.opsForValue().get("a"));
  }

  @Test
  public void test2() {
    assertEquals("1", redisTemplate.opsForValue().get("a"));
  }
}
