package com.example;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.List;

public class LettuceMonoTest {

  @Test
  public void test(){
    RedisClient redisClient = RedisClient.create(RedisURI.create("localhost", 6379));

    StatefulRedisConnection<String, String> conn = redisClient.connect();
    conn.setAutoFlushCommands(false);
    RedisReactiveCommands<String, String> commands = conn.reactive();


  }

  Mono<Holder> doit(RedisReactiveCommands<String, String> commands, int value){
    return load0(commands)
        .map(holder -> {
            holder.intValue += value;
            return holder;
        })
        .flatMap(holder -> load1(commands, holder))
        .map(holder -> {
            holder.stringValue += value;
            return holder;
        })
        .flatMap(holder -> load2(commands, holder))
        .map(holder -> {
            holder.listValue.add(value+"");
            return holder;
        });
  }

  Mono<Holder> load0(RedisReactiveCommands<String, String> commands){
    return commands.get("intValue").map(str -> {
      Holder holder = new Holder();
      holder.intValue = Integer.parseInt(str);
      return holder;
    });
  }

  Mono<Holder> load1(RedisReactiveCommands<String, String> commands, Holder holder){
    return commands.get("stringValue").map(str -> {
      holder.stringValue = str;
      return holder;
    });
  }

  Mono<Holder> load2(RedisReactiveCommands<String, String> commands, Holder holder){
    return commands.lrange("listValue", 0, -1).collectList().map(list -> {
      holder.listValue = list;
      return holder;
    });
  }

};

class Holder{
  int intValue;
  String stringValue;
  List<String> listValue;
}
