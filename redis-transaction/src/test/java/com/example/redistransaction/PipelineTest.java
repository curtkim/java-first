package com.example.redistransaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Import({RedisTxContextConfig.class})
@DataRedisTest
public class PipelineTest {

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
  public void testNestedPipeline(){
    redisTemplate.execute(new SessionCallback<Object>() {
      @Override
      public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
        operations.multi();
        operations.opsForValue().set((K)"a", (V)"A");
        operations.opsForValue().set((K)"b", (V)"B");
        operations.exec();

        List<Object> results = operations.executePipelined(new SessionCallback<Object>() {
          @Override
          public <K, V> Object execute(RedisOperations<K, V> opInner) throws DataAccessException {
            opInner.opsForValue().get("a");
            opInner.opsForValue().get("b");
            return null;
          }
        });
        assertEquals(Arrays.asList("A", "B"), results);
        return null;
      }
    });
  }
}
