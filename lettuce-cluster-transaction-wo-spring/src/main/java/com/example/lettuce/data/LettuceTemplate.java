package com.example.lettuce.data;

import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.SlotHash;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;

@AllArgsConstructor
@Slf4j
public class LettuceTemplate<T> {

  GenericObjectPool<StatefulConnection<String, String>> pool;

  public T execute(String sampleKey, LettuceCallback<T> callback){

    StatefulRedisClusterConnection<String, String> conn = (StatefulRedisClusterConnection<String, String>)LettuceConnectionUtils.getConnection(pool);
    RedisClusterNode node = conn.getPartitions().getPartitionBySlot(SlotHash.getSlot(sampleKey));
    log.info("targetNode={} key={}", node, sampleKey);
    StatefulRedisConnection<String, String> nodeConn = conn.getConnection(node.getNodeId());

    try {
      log.info("pool active count = {}", pool.getNumActive());
      RedisCommands<String, String> commands = nodeConn.sync();
      return callback.execute(commands);
    } finally {
      LettuceConnectionUtils.unbindConnection(pool);
      log.info("pool active count = {}", pool.getNumActive());
    }

    /*
    try (StatefulRedisConnection<String, String> conn= pool.borrowObject()) {
      callback.execute(conn);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    log.info("pool active count = {}", pool.getNumActive());
    */
  }


}
