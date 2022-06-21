package lettuce;

import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import lombok.AllArgsConstructor;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;


@AllArgsConstructor
public class MultiDao {

  private static final Logger logger = LoggerFactory.getLogger(MultiDao.class);

  private GenericObjectPool<StatefulRedisConnection<String, String>> pool;

  Mono<TransactionResult> doMulti(){
    StatefulRedisConnection<String, String> conn = null;
    try {
      conn = pool.borrowObject();
    } catch (Exception e) {
      return Mono.error(e);
    }
    final StatefulRedisConnection<String, String> finalConn = conn;

    RedisReactiveCommands<String, String> cmd = conn.reactive();

    logger.info("multi {}", conn.isOpen());
    return cmd.multi()
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
          pool.returnObject(finalConn);
        });
  }
}
