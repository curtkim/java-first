package lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PoolAndMultiReactiveMain {

  public static void main(String[] args) {
    RedisURI uri = RedisURI.create("localhost", 6379);
    RedisClient client = RedisClient.create(uri);

    GenericObjectPool<StatefulRedisConnection<String, String>> pool =
        ConnectionPoolSupport.createGenericObjectPool(() -> client.connect(), new GenericObjectPoolConfig(), true);

    Mono.using(
        () -> pool.borrowObject(),
        (c) -> Mono.just(c.reactive()),
        (c) -> {
          System.out.println("connection close");
          //pool.returnObject(c);
          //c.close();
        },
        false // set to true to clean before any signal (including onNext) is passed downstream
    ).flatMap(commands -> {
      return commands.multi().doOnSuccess(s -> {
        commands.set("key", "1").subscribe();
        commands.incr("key").subscribe();
      }).flatMap(s -> {
        System.out.println("commands exec");
        return commands.exec();
      });
    }).subscribe((result)->{
      System.out.println((String) result.get(0));
      System.out.println((Long) result.get(1));
    });

    // terminating
    pool.close();
    client.shutdown();
  }
}
