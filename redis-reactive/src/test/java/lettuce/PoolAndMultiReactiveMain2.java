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


public class PoolAndMultiReactiveMain2 {

  private static final Logger logger = LoggerFactory.getLogger(PoolAndMultiReactiveMain2.class);

  static Mono<TransactionResult> doMultiAndExec(RedisReactiveCommands<String, String> cmd) {
    // cmd를 closure안에서 사용하지 않기 때문에 lazy하지 않다.
    cmd.multi().subscribe();
    cmd.get("key").subscribe();
    cmd.get("key").subscribe();
    return cmd.exec();

    /*
    return cmd.multi()
        .doOnSuccess(s -> {
          cmd.get("key").subscribe();
          cmd.get("key").subscribe();
        }).flatMap(s -> {
          return cmd.exec();
        });
     */
  }

  public static void main(String[] args) {
    RedisURI uri = RedisURI.create("localhost", 6379);
    RedisClient client = RedisClient.create(uri);

    GenericObjectPool<StatefulRedisConnection<String, String>> pool =
        ConnectionPoolSupport.createGenericObjectPool(() -> client.connect(), new GenericObjectPoolConfig(), false);

    Mono.using(
            () -> pool.borrowObject(),
            (c) -> Mono.just(c.reactive()),
            (c) -> {
              logger.info("connection close");
              pool.returnObject(c);
              //c.close();
            },
            false // set to true to clean before any signal (including onNext) is passed downstream
        )
        .log()
//        .flatMap(cmd -> {
//          logger.info("get {}", cmd.getStatefulConnection().isOpen());
//          return cmd.get("key");
//        })
        .flatMap(cmd -> {
          logger.info("doMultiAndExec {}", cmd.getStatefulConnection().isOpen());
          return doMultiAndExec(cmd);
        })
        .subscribe(
            (TransactionResult result) -> {
              logger.info("subscribe");
              logger.info(result.toString());
            }
        );

    // terminating
    pool.close();
    client.shutdown();
  }
}
