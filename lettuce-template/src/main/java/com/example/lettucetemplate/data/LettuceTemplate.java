package com.example.lettucetemplate.data;

import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.SlotHash;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnectionExtensionsKt;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;

@Slf4j
@AllArgsConstructor
public class LettuceTemplate<K, V> {

  GenericObjectPool<StatefulConnection<K, V>> pool;

  public <T> T execute(String sampleKey, LettuceStandaloneCallback<K, V> callback) {
    StatefulConnection<K, V> conn = null;
    try {
      conn = pool.borrowObject();
      if (conn instanceof StatefulRedisClusterConnection<K, V>) {
        StatefulRedisClusterConnection<K, V> clusterConn = (StatefulRedisClusterConnection<K, V>) conn;
        RedisClusterNode node = clusterConn.getPartitions().getPartitionBySlot(SlotHash.getSlot(sampleKey));
        log.info("targetNode={} key={}", node, sampleKey);
        StatefulRedisConnection<K, V> nodeConn = clusterConn.getConnection(node.getNodeId());
        RedisCommands<K, V> commands = nodeConn.sync();
        return callback.execute(commands, commands, commands, commands);
      }
      else if( conn instanceof StatefulConnection<K,V>){
        StatefulRedisConnection<K, V> redisConn = (StatefulRedisConnection<K, V>)conn;
        RedisCommands<K, V> commands = redisConn.sync();
        return callback.execute(commands, commands, commands, commands);
      }
      else{
        throw new RuntimeException("not here");
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    } finally {
      conn.close();
    }
  }

  public <T> T execute(LettuceCallback<K, V> callback){
    StatefulConnection<K, V> conn = null;
    try {
      conn = pool.borrowObject();
      if (conn instanceof StatefulRedisClusterConnection<K, V>) {
        StatefulRedisClusterConnection<K, V> clusterConn = (StatefulRedisClusterConnection<K, V>) conn;
        RedisAdvancedClusterCommands<K, V> commands = clusterConn.sync();
        return callback.execute(commands, commands, commands);
      }
      else if( conn instanceof StatefulConnection<K,V>){
        StatefulRedisConnection<K, V> redisConn = (StatefulRedisConnection<K, V>)conn;
        RedisCommands<K, V> commands = redisConn.sync();
        return callback.execute(commands, commands, commands);
      }
      else{
        throw new RuntimeException("not here");
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    } finally {
      conn.close();
    }
  }

}
