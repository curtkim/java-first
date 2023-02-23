package com.example;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RedisClusterTest {
  static {
    DockerComposeContainer environment =
      new DockerComposeContainer(new File("src/test/resources/docker-compose.yml"));
    environment.start();

//    int REDIS_PORT = 6379;
//    System.out.println(environment.getServiceHost("node01", REDIS_PORT));
//    System.out.println(environment.getServiceHost("node02", REDIS_PORT));
  }

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
