package com.example.lettucetemplate;


import com.example.lettucetemplate.data.*;
import com.redis.testcontainers.RedisClusterContainer;
import io.lettuce.core.*;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.async.RedisKeyAsyncCommands;
import io.lettuce.core.api.async.RedisListAsyncCommands;
import io.lettuce.core.api.async.RedisStringAsyncCommands;
import io.lettuce.core.api.async.RedisTransactionalAsyncCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class LettuceTemplateClusterAsyncTest {

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
    lettuceTemplate.executeAsync(key, new LettuceStandaloneAsyncCallback<String, String>() {
      @Override
      public <Object> Object execute(
          RedisStringAsyncCommands<String, String> stringCommands,
          RedisListAsyncCommands<String, String> listCommands,
          RedisKeyAsyncCommands<String, String> keyCommands,
          RedisTransactionalAsyncCommands<String, String> txCommands) {

        RedisFuture<String> a = txCommands.watch(key);
        RedisFuture<String> b = txCommands.multi();
        RedisFuture<String> c = stringCommands.set(key, "1");
        RedisFuture<String> d = stringCommands.set(key2, "2");
        RedisFuture<TransactionResult> e = txCommands.exec();
        LettuceFutures.awaitAll(1, TimeUnit.MINUTES, a, b, c, d, e);

        try {
          if( e.get().wasDiscarded())
            throw new RuntimeException("fail optimistic lock");
        } catch (InterruptedException ex) {
          throw new RuntimeException(ex);
        } catch (ExecutionException ex) {
          throw new RuntimeException(ex);
        }
        return null;
      }
    });

    lettuceTemplate.executeAsync(key, new LettuceStandaloneAsyncCallback<String, String>() {
      @Override
      public <T> T execute(
          RedisStringAsyncCommands<String, String> stringCommands,
          RedisListAsyncCommands<String, String> listCommands,
          RedisKeyAsyncCommands<String, String> keyCommands,
          RedisTransactionalAsyncCommands<String, String> txCommands) {

        try {
          assertEquals("1", stringCommands.get(key).get());
          assertEquals("2", stringCommands.get(key2).get());
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        } catch (ExecutionException e) {
          throw new RuntimeException(e);
        }
        return null;
      }
    });

    /*
    lettuceTemplate.executeAsync(new LettuceAsyncCallback<String, String>() {
      @Override
      public <T> T execute(RedisStringAsyncCommands<String, String> stringCommands,
                           RedisListAsyncCommands<String, String> listCommands,
                           RedisKeyAsyncCommands<String, String> keyCommands) {
        final int SCAN_COUNT = 100;
        ScanIterator<String> iter = ScanIterator.scan(keyCommands, ScanArgs.Builder.limit(SCAN_COUNT).match("prefix*"));
        List<String> first = Arrays.asList(key, key2);
        List<String> second=  iter.stream().collect(Collectors.toList());
        assertTrue(first.size() == second.size() && first.containsAll(second) && second.containsAll(first));
        return null;
      }
    });
    */
  }
}
