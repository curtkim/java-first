package lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class PoolAndMultiMain {

  public static void main(String[] args){
    RedisURI uri = RedisURI.create("localhost", 6379);
    RedisClient client = RedisClient.create(uri);

    GenericObjectPool<StatefulRedisConnection<String, String>> pool =
        ConnectionPoolSupport.createGenericObjectPool(() -> client.connect(), new GenericObjectPoolConfig());

    TransactionResult result = null;
    // executing work
    try (StatefulRedisConnection<String, String> connection = pool.borrowObject()) {
      RedisCommands<String, String> commands = connection.sync();
      commands.multi();
      commands.set("key", "value");
      commands.set("key2", "value2");
      result = commands.exec();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    System.out.println((String) result.get(0));
    System.out.println((String) result.get(1));

    // terminating
    pool.close();
    client.shutdown();
  }
}
