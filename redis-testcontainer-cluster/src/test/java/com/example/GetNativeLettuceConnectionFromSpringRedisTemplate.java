package com.example;

import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.SlotHash;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.async.RedisClusterAsyncCommands;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import com.redis.testcontainers.RedisClusterContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceClusterConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
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
public class GetNativeLettuceConnectionFromSpringRedisTemplate {

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
  public void getNativeLettuceConnection(){
    String key = "{user101}/drive";
    String key2 = "{user101}/path";

    redisTemplate.execute(new RedisCallback<Object>() {
      @Override
      public Object doInRedis(RedisConnection conn) throws DataAccessException {
        /*
        org.springframework.data.redis.connection.DefaultStringRedisConnection redisConn = (org.springframework.data.redis.connection.DefaultStringRedisConnection)conn;
        org.springframework.data.redis.connection.lettuce.LettuceClusterConnection lettuceConn = (LettuceClusterConnection) redisConn.getDelegate();


        io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands commands0 = (RedisAdvancedClusterAsyncCommands)lettuceConn.getNativeConnection();
        StatefulRedisClusterConnection statefulRedisClusterConnection = commands0.getStatefulConnection();
        RedisClusterNode node0 = statefulRedisClusterConnection.getPartitions().getPartitionBySlot(SlotHash.getSlot(key));
        io.lettuce.core.api.StatefulRedisConnection slotConn = statefulRedisClusterConnection.getConnection(node0.getNodeId());

        org.springframework.data.redis.connection.DefaultStringRedisConnection slotRedisConn = new DefaultStringRedisConnection(new LettuceConnection(slotConn, 1000))

        //LettuceConnection lettuceConnection = new LettuceConnection(statefulRedisClusterConnection);

        RedisTemplate innerTemplate = new RedisTemplate();
        innerTemplate.setConnectionFactory();
        */


        RedisAdvancedClusterAsyncCommands commands = (io.lettuce.core.cluster.RedisAdvancedClusterAsyncCommandsImpl)conn.getNativeConnection();
        RedisClusterNode node = commands.getStatefulConnection().getPartitions().getPartitionBySlot(SlotHash.getSlot(key));
        RedisCommands<byte[], byte[]> cmds = commands.getStatefulConnection().getConnection(node.getNodeId()).sync();

        System.out.println(node.getNodeId() + " " +node);
        {
          cmds.multi();
          cmds.set(key.getBytes(), "1".getBytes());
          cmds.set(key2.getBytes(), "2".getBytes());
          TransactionResult result = cmds.exec();

          System.out.println(result);
          result.stream().forEach(it -> System.out.println(it));
        }
        return null;
      }
    }, true);

    assertEquals("1", redisTemplate.opsForValue().get(key));
    assertEquals("2", redisTemplate.opsForValue().get(key2));
  }
}
