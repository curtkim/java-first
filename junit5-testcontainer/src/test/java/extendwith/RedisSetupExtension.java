package extendwith;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public class RedisSetupExtension implements BeforeAllCallback, AfterAllCallback {

  public static int REDIS_PORT = 6379;

  @Container
  public static final GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:6.2.6"))
      .withAccessToHost(true)
      .withExposedPorts(REDIS_PORT)
      .withReuse(true);


  @Override
  public void beforeAll(ExtensionContext context) {
    System.out.println("redis start");
    redis.start();
  }

  @Override
  public void afterAll(ExtensionContext context) throws Exception {
    redis.stop();
    System.out.println("redis stop");
  }
}
