package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class RedisTest {
  static {
    GenericContainer<?> redis =
        new GenericContainer<>(DockerImageName.parse("redis:7.0.8-alpine")).withExposedPorts(6379);
    redis.start();
    System.setProperty("spring.redis.host", redis.getHost());
    System.setProperty("spring.redis.port", redis.getMappedPort(6379).toString());
    System.out.println("redis port=" + redis.getMappedPort(6379));
  }

  @Autowired
  private StringRedisTemplate redisTemplate;


  @Test
  public void testSimplePutAndGet() {
    redisTemplate.opsForValue().set("test", "example");
    String retrieved = redisTemplate.opsForValue().get("test");
    assertThat(retrieved).isEqualTo("example");
  }
}
