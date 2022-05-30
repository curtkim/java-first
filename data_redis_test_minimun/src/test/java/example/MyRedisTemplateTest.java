package example;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Import(EmbededRedisConfig.class)
@DataRedisTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class MyRedisTemplateTest {
  @Autowired
  private RedisTemplate redisTemplate;

  @Test
  public void t1_set(){
    redisTemplate.opsForValue().set("a", "1");
  }

  @Test
  public void t2_get(){
    assertEquals("1", redisTemplate.opsForValue().get("a"));
  }
}
