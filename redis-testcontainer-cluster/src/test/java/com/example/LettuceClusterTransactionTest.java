package com.example;

import com.redis.testcontainers.RedisClusterContainer;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.SlotHash;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
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
        .adaptiveRefreshTriggersTimeout(Duration.ofSeconds(30))
        .build();

    clusterClient.setOptions(ClusterClientOptions.builder()
        .topologyRefreshOptions(topologyRefreshOptions)
        .build());

    GenericObjectPool<StatefulRedisClusterConnection<String, String>> pool = ConnectionPoolSupport.createGenericObjectPool(
        () -> clusterClient.connect(),
        new GenericObjectPoolConfig()
    );

    // https://groups.google.com/g/lettuce-redis-client-users/c/llH4sfkUj8k
    String key = "{user101}/drive";
    String key2 = "{user101}/path";

    try (StatefulRedisClusterConnection<String, String> connection = pool.borrowObject()) {
      RedisClusterNode node = connection.getPartitions().getPartitionBySlot(SlotHash.getSlot(key));
      System.out.println(node);
      RedisCommands<String, String> commands = connection.getConnection(node.getNodeId()).sync();
      {
        commands.multi();
        commands.set(key, "1");
        commands.set(key2, "2");
        TransactionResult result = commands.exec();

        System.out.println(result);
        result.stream().forEach(it -> System.out.println(it));
      }
    }

    pool.close();
    clusterClient.shutdown();
  }

}
