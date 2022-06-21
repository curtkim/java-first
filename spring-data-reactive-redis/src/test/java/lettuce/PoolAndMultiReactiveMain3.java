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
import reactor.core.publisher.Mono;


public class PoolAndMultiReactiveMain3 {

  private static final Logger logger = LoggerFactory.getLogger(PoolAndMultiReactiveMain3.class);

  public static void main(String[] args) throws Exception {
    RedisURI uri = RedisURI.create("localhost", 6379);
    RedisClient client = RedisClient.create(uri);

    GenericObjectPool<StatefulRedisConnection<String, String>> pool =
        ConnectionPoolSupport.createGenericObjectPool(() -> client.connect(), new GenericObjectPoolConfig(), true);

    RedisReactiveCommands<String, String> cmd = pool.borrowObject().reactive();

    logger.info("multi");
    cmd.multi()
        .doOnSuccess(s -> {
          logger.info("multi inner");       // multi와는 다른 thread에서 실행된다. lettuce가 생성한 thread에서 실행된다.
          cmd.set("key", "1").subscribe();
          cmd.incr("key").subscribe();
        }).flatMap(s -> {
          logger.info("exec");
          return cmd.exec();
        })
        .log()
        .doFinally(s -> {
          logger.info("doFinally return Object");
          pool.returnObject(cmd.getStatefulConnection());
        })
        .subscribe(
            (TransactionResult result) -> {
              logger.info("subscribe");
              logger.info("{}, {}", result.<String>get(0), result.<Long>get(1));
            }
        );

    Thread.sleep(2000);
    logger.info("pool close");
    pool.close();
    client.shutdown();
  }
}
