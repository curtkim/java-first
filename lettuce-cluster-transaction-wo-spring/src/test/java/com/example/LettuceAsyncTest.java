package com.example;

import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LettuceAsyncTest {
  static String OK = "OK";

  @Test
  public void test() throws ExecutionException, InterruptedException {
    RedisClient redisClient = RedisClient.create(RedisURI.create("localhost", 6379));

    StatefulRedisConnection<String, String> conn = redisClient.connect();
    conn.setAutoFlushCommands(false);
    RedisAsyncCommands<String, String> cmds = conn.async();

    RedisFuture<String> a = cmds.set("a", "1");
    RedisFuture<String> b = cmds.set("b", "2");
    cmds.flushCommands();

    boolean result = LettuceFutures.awaitAll(5, TimeUnit.SECONDS, a, b);
    if(result){
      assertEquals(OK, a.get());
      assertEquals(OK, b.get());
    }

  }
}
