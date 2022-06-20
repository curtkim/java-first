package tx;

import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

public class MyService {

  @Autowired
  private RedisReactiveCommands<String, String> commands;

  public Mono<TransactionResult> addAndCount(String key, String value) {
    return commands.multi()
        .doOnNext(multi -> {
          commands.sadd(key, value).subscribe();
          commands.scard(key).subscribe();
        })
        .flatMap(multi -> commands.exec());
  }
}
