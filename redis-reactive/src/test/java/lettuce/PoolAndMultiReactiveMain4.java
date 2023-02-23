package lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PoolAndMultiReactiveMain4 {

  private static final Logger logger = LoggerFactory.getLogger(PoolAndMultiReactiveMain4.class);

  public static void main(String[] args) throws Exception {
    RedisURI uri = RedisURI.create("localhost", 6379);
    RedisClient client = RedisClient.create(uri);

    GenericObjectPool<StatefulRedisConnection<String, String>> pool =
        ConnectionPoolSupport.createGenericObjectPool(() -> client.connect(), new GenericObjectPoolConfig(), false);

    MultiDao dao = new MultiDao(pool);

    dao.doMulti()
        .subscribe(
            (TransactionResult result) -> {
              logger.info("subscribe");
              logger.info("wasDiscarded={} [{}, {}]", result.wasDiscarded(), result.<String>get(0), result.<Long>get(1));
            }
        );

    Thread.sleep(2000);
    logger.info("pool close");
    pool.close();
    client.shutdown();
  }
}
