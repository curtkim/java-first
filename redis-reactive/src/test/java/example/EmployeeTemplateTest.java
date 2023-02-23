package example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;


@Import({EmbededRedisConfig.class, EmployeeTemplateConfig.class})
@DataRedisTest
public class EmployeeTemplateTest {

  @Autowired
  private ReactiveRedisTemplate<String, Employee> redisTemplate;
  private ReactiveValueOperations<String, Employee> reactiveValueOps;


  @BeforeEach
  public void setup() {
    reactiveValueOps = redisTemplate.opsForValue();
  }

  @Test
  public void givenEmployee_whenSet_thenSet() {
    Mono<Boolean> result = reactiveValueOps.set("123", new Employee("123", "Bill", "Accounts"));
    StepVerifier.create(result)
        .expectNext(true)
        .verifyComplete();
  }

  @Test
  public void givenEmployeeId_whenGet_thenReturnsEmployee() {
    Mono<Employee> fetchedEmployee = reactiveValueOps.get("123");
    StepVerifier.create(fetchedEmployee)
        .expectNext(new Employee("123", "Bill", "Accounts"))
        .verifyComplete();
  }

  @Test
  public void givenEmployee_whenSetWithExpiry_thenSetsWithExpiryTime()
      throws InterruptedException {
    Mono<Boolean> result = reactiveValueOps.set("129",
        new Employee("129", "John", "Programming"),
        Duration.ofSeconds(1));
    StepVerifier.create(result)
        .expectNext(true)
        .verifyComplete();
    Thread.sleep(2000L);
    Mono<Employee> fetchedEmployee = reactiveValueOps.get("129");
    StepVerifier.create(fetchedEmployee)
        .expectNextCount(0L)
        .verifyComplete();
  }
}
