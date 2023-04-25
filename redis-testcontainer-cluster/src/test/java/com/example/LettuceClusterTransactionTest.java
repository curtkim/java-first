package com.example;

import com.redis.testcontainers.RedisClusterContainer;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@SpringBootTest
@Testcontainers
public class LettuceClusterTransactionTest {

  @Container
  static RedisClusterContainer REDIS_CLUSTER = new RedisClusterContainer(
      RedisClusterContainer.DEFAULT_IMAGE_NAME.withTag("6.2.10")); // 7.x.x를 사용하면 auth관련 DENIED error가 발생해서..

  @Test
  public void test() throws Exception {
    String[] redisUris = REDIS_CLUSTER.getRedisURIs();

    List<RedisURI> nodes = Arrays.stream(redisUris).map(it -> RedisURI.create(it)).collect(Collectors.toList());
    RedisClusterClient clusterClient = RedisClusterClient.create(nodes);

    ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
        .enableAdaptiveRefreshTrigger(ClusterTopologyRefreshOptions.RefreshTrigger.MOVED_REDIRECT, ClusterTopologyRefreshOptions.RefreshTrigger.PERSISTENT_RECONNECTS)
        //.adaptiveRefreshTriggersTimeout(30, TimeUnit.SECONDS)
        .build();

    clusterClient.setOptions(ClusterClientOptions.builder()
        .topologyRefreshOptions(topologyRefreshOptions)
        .build());

    GenericObjectPool<StatefulRedisClusterConnection<String, String>> pool = ConnectionPoolSupport.createGenericObjectPool(
        () -> clusterClient.connect(),
        new GenericObjectPoolConfig()
    );

    try (StatefulRedisClusterConnection<String, String> connection = pool.borrowObject()) {
      connection.sync().set("key", "value");
    }

    pool.close();
    clusterClient.shutdown();
  }

}
