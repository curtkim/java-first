package simple;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class RedisTest {

  static int REDIS_PORT = 6379;

  @Container
  static final GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:6.2.6"))
      .withAccessToHost(true)
      .withExposedPorts(REDIS_PORT)
      .withReuse(true);

  @Test
  public void test(){
    String redisUri = String.format("redis://%s:%d",redis.getHost(), redis.getMappedPort(REDIS_PORT));
    RedisClient redisClient = RedisClient.create(redisUri);
    StatefulRedisConnection<String, String> connection = redisClient.connect();

    RedisCommands<String, String> cmd = connection.sync();
    cmd.set("a","1");
    assertEquals("1", cmd.get("a"));
  }

}
