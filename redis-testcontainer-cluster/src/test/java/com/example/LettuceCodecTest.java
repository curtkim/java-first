package com.example;

import com.redis.testcontainers.RedisClusterContainer;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Testcontainers
public class LettuceCodecTest {

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
        .adaptiveRefreshTriggersTimeout(Duration.ofSeconds(30))
        .build();

    clusterClient.setOptions(ClusterClientOptions.builder()
        .topologyRefreshOptions(topologyRefreshOptions)
        .build());

    GenericObjectPool<StatefulRedisClusterConnection<String, Object>> pool = ConnectionPoolSupport.createGenericObjectPool(
        () -> clusterClient.connect(new SerializedObjectCodec()),
        new GenericObjectPoolConfig()
    );

    try (StatefulRedisClusterConnection<String, Object> connection = pool.borrowObject()) {
      RedisAdvancedClusterCommands<String, Object> commands = connection.sync();
      commands.set("key1", new Date(1000));
    }

    try (StatefulRedisClusterConnection<String, Object> connection = pool.borrowObject()) {
      RedisAdvancedClusterCommands<String, Object> commands = connection.sync();
      Date obj = (Date)commands.get("key1");
      assertEquals(new Date(1000), obj);
    }

    pool.close();
    clusterClient.shutdown();
  }
}
