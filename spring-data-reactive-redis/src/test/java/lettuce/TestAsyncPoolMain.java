package lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.support.AsyncConnectionPoolSupport;
import io.lettuce.core.support.AsyncPool;
import io.lettuce.core.support.BoundedPoolConfig;
import reactor.core.publisher.Mono;

public class TestAsyncPoolMain {
  public static void main(String[] args){

    RedisURI uri = RedisURI.create("localhost", 6379);
    RedisClient client = RedisClient.create(uri);
    AsyncPool<StatefulRedisConnection<String, String>> pool = AsyncConnectionPoolSupport.createBoundedObjectPool(
        () -> client.connectAsync(StringCodec.ASCII, uri), BoundedPoolConfig.create(), true);

    Mono.fromFuture(pool.acquire())
        .flatMap(conn -> {
          RedisReactiveCommands<String, String> commands = conn.reactive();
          return commands.multi().doOnSuccess(s-> {
            commands.set("key", "1").subscribe();
            commands.incr("key").subscribe();
          }).flatMap(s-> commands.exec())
              .doOnNext(transationResult -> {
                System.out.println((String)transationResult.get(0));
                System.out.println((Long)transationResult.get(1));
              });
        }).block();
    // conn을 어떻게 반환해야 하는지 모르겠음.

  }
}
