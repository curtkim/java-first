package example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Import(EmbededRedisConfig.class)
@DataRedisTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class MyReactiveRedisTemplateTest {
  private static final String LIST_NAME = "demo_list";

  @Autowired
  private ReactiveStringRedisTemplate redisTemplate;
  private ReactiveListOperations<String, String> reactiveListOps;

  @BeforeEach
  public void setup() {
    reactiveListOps = redisTemplate.opsForList();
  }

  @Test
  public void givenListAndValues_whenLeftPushAndLeftPop_thenLeftPushAndLeftPop() {
    Mono<Long> lPush = reactiveListOps.leftPushAll(LIST_NAME, "first", "second")
        .log("Pushed");

    StepVerifier.create(lPush)
        .expectNext(2L)
        .verifyComplete();

    Mono<String> lPop = reactiveListOps.leftPop(LIST_NAME)
        .log("Popped");

    StepVerifier.create(lPop)
        .expectNext("second")
        .verifyComplete();
  }
}
