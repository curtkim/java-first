package extendwith;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(RedisSetupExtension.class)
public class RedisTest2 {

  @Test
  public void test(){
    String redisUri = String.format("redis://%s:%d", RedisSetupExtension.redis.getHost(), RedisSetupExtension.redis.getMappedPort(RedisSetupExtension.REDIS_PORT));
    RedisClient redisClient = RedisClient.create(redisUri);
    StatefulRedisConnection<String, String> connection = redisClient.connect();

    RedisCommands<String, String> cmd = connection.sync();
    cmd.set("b","2");
    assertEquals("2", cmd.get("b"));
  }

}
