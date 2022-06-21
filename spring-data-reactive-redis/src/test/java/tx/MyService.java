package tx;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.support.AsyncConnectionPoolSupport;
import io.lettuce.core.support.AsyncPool;
import io.lettuce.core.support.BoundedPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import reactor.core.publisher.Mono;

public class MyService {

  @Autowired
  private RedisReactiveCommands<String, String> commands;

  public Mono<TransactionResult> addAndCount(String key, String value) {


//    RedisURI uri = RedisURI.Builder.redis("localhost", 6379).build();
//    RedisClient client = null;
//    AsyncPool<StatefulRedisConnection<String, String>> pool = AsyncConnectionPoolSupport.createBoundedObjectPool(
//        () -> client.connectAsync(StringCodec.ASCII, uri), BoundedPoolConfig.create(), false);
//
//    Mono.fromFuture(pool.acquire()).
    //RedisClient redisClient = null;
    //commands = redisClient.connect().reactive();

    return commands.multi()
        .doOnNext(multi -> {
          commands.sadd(key, value).subscribe();
          commands.scard(key).subscribe();
        })
        .flatMap(multi -> commands.exec());
  }
}
