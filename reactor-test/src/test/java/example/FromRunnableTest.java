package example;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FromRunnableTest {
  @Test
  public void test() {

    StepVerifier.create(
        Mono.fromRunnable(() -> System.out.println("a"))
    )
        .expectSubscription()
        .expectComplete();

  }
}
