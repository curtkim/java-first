package com.example.lettucetemplate;


import com.example.lettucetemplate.data.LettuceCallback;
import com.example.lettucetemplate.data.LettuceStandaloneCallback;
import com.example.lettucetemplate.data.LettuceTemplate;
import com.redis.testcontainers.RedisClusterContainer;
import io.lettuce.core.RedisURI;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanIterator;
import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.sync.RedisKeyCommands;
import io.lettuce.core.api.sync.RedisListCommands;
import io.lettuce.core.api.sync.RedisStringCommands;
import io.lettuce.core.api.sync.RedisTransactionalCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class LettuceTemplateClusterTest {

  @Container
  static RedisClusterContainer REDIS_CLUSTER = new RedisClusterContainer(
      RedisClusterContainer.DEFAULT_IMAGE_NAME.withTag("6.2.10")
  )
      //.withStartupTimeout(Duration.ofSeconds(90))
      .withReuse(true);


  @Test
  public void test() {
    RedisURI node1 = RedisURI.create("localhost", 7000);
    RedisURI node2 = RedisURI.create("localhost", 7001);
    RedisURI node3 = RedisURI.create("localhost", 7002);

    RedisClusterClient clusterClient = RedisClusterClient.create(Arrays.asList(node1, node2, node3));

    GenericObjectPool<StatefulConnection<String, String>> pool = ConnectionPoolSupport
        .createGenericObjectPool(() -> clusterClient.connect(), new GenericObjectPoolConfig());

    LettuceTemplate<String, String> lettuceTemplate = new LettuceTemplate<>(pool);

    String key = "prefix:{a}";
    String key2 = "prefix2:{a}";
    lettuceTemplate.execute(key, new LettuceStandaloneCallback<String, String>() {
      @Override
      public <Object> Object execute(
          RedisStringCommands<String, String> stringCommands,
          RedisListCommands<String, String> listCommands,
          RedisKeyCommands<String, String> keyCommands,
          RedisTransactionalCommands<String, String> txCommands) {
        txCommands.watch(key);
        txCommands.multi();
        stringCommands.set(key, "1");
        stringCommands.set(key2, "2");
        TransactionResult result = txCommands.exec();
        if( result.wasDiscarded())
          throw new RuntimeException("fail optimistic lock");
        return null;
      }
    });

    lettuceTemplate.execute(key, new LettuceStandaloneCallback<>() {
      @Override
      public <T> T execute(
          RedisStringCommands<String, String> stringCommands,
          RedisListCommands<String, String> listCommands,
          RedisKeyCommands<String, String> keyCommands,
          RedisTransactionalCommands<String, String> txCommands) {
        assertEquals("1", stringCommands.get(key));
        assertEquals("2", stringCommands.get(key2));
        return null;
      }
    });

    lettuceTemplate.execute(new LettuceCallback<>() {
      @Override
      public <T> T execute(RedisStringCommands<String, String> stringCommands, RedisListCommands<String, String> listCommands, RedisKeyCommands<String, String> keyCommands) {
        final int SCAN_COUNT = 100;
        ScanIterator<String> iter = ScanIterator.scan(keyCommands, ScanArgs.Builder.limit(SCAN_COUNT).match("prefix*"));
        List<String> first = Arrays.asList(key, key2);
        List<String> second=  iter.stream().collect(Collectors.toList());
        assertTrue(first.size() == second.size() && first.containsAll(second) && second.containsAll(first));
        return null;
      }
    });
  }
}
