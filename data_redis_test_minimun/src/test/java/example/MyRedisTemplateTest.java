package example;

import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Import(EmbededRedisConfig.class)
@DataRedisTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class MyRedisTemplateTest {
  @Autowired
  private RedisTemplate redisTemplate;

  @Test
  public void test(){
    redisTemplate.executeWithStickyConnection(new RedisCallback() {
      @Override
      public Object doInRedis(RedisConnection connection) throws DataAccessException {
        RedisAsyncCommands commands = (RedisAsyncCommands)connection.getNativeConnection();
        System.out.println(connection.getNativeConnection().getClass().getName());
        return null;
      }
    });
  }

  @Test
  public void t1_set(){
    redisTemplate.opsForValue().set("a", "1");
  }

  @Test
  public void t2_get(){
    assertEquals("1", redisTemplate.opsForValue().get("a"));
  }
}
