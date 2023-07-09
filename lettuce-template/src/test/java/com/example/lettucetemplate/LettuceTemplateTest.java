package com.example.lettucetemplate;

import com.example.lettucetemplate.data.LettuceCallback;
import com.example.lettucetemplate.data.LettuceTemplate;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisKeyCommands;
import io.lettuce.core.api.sync.RedisListCommands;
import io.lettuce.core.api.sync.RedisStringCommands;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class LettuceTemplateTest {
  @Container
  static GenericContainer redis = new GenericContainer(DockerImageName.parse("redis:7.0.8-alpine"))
      .withExposedPorts(6379);

  @Test
  public void test(){
    RedisClient client = RedisClient.create(RedisURI.create("localhost", redis.getMappedPort(6379)));

    GenericObjectPool<StatefulConnection<String, String>> pool = ConnectionPoolSupport
        .createGenericObjectPool(() -> client.connect(), new GenericObjectPoolConfig());
    LettuceTemplate lettuceTemplate = new LettuceTemplate(pool);

    lettuceTemplate.execute(new LettuceCallback() {
      @Override
      public Object execute(RedisStringCommands stringCommands, RedisListCommands listCommands, RedisKeyCommands keyCommands) {
        stringCommands.set("a", "1");
        return null;
      }
    });

    lettuceTemplate.execute(new LettuceCallback() {
      @Override
      public Object execute(RedisStringCommands stringCommands, RedisListCommands listCommands, RedisKeyCommands keyCommands) {
        assertEquals("1", stringCommands.get("a"));
        return null;
      }
    });
  }

}
