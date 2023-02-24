package com.example;

import com.redis.testcontainers.RedisClusterContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class RedisClusterTest {

  @Container
  static RedisClusterContainer REDIS_CLUSTER = new RedisClusterContainer(
      RedisClusterContainer.DEFAULT_IMAGE_NAME.withTag("6.2.10")); // 7.x.x를 사용하면 auth관련 DENIED error가 발생해서..

  @DynamicPropertySource
  static void registerPgProperties(DynamicPropertyRegistry registry) {
    String redisNodes = Arrays.stream(REDIS_CLUSTER.getRedisURIs())
        .map(uri -> uri.replace("redis://", ""))
        .collect(Collectors.joining(","));
    registry.add("spring.redis.cluster.nodes", () -> redisNodes);
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
