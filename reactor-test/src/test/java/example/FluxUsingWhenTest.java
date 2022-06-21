package example;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.*;

public class FluxUsingWhenTest {

  @Test
  public void empty(){
    AtomicBoolean commitDone = new AtomicBoolean();
    AtomicBoolean rollbackDone = new AtomicBoolean();

    Flux<String> test = Flux.usingWhen(
        Flux.empty(),
        tr -> Mono.just("unexpected"),
        tr -> Mono.fromRunnable(() -> commitDone.set(true)),
        (tr, err) -> Mono.fromRunnable(() -> rollbackDone.set(true)),
        tr -> Mono.fromRunnable(() -> rollbackDone.set(true))
    );

    StepVerifier.create(test)
        .verifyComplete();

    assertThat(commitDone).isFalse();
    assertThat(rollbackDone).isFalse();
  }


  @Test
  public void fluxResourcePublisherIsCancelled() {
    AtomicBoolean cancelled = new AtomicBoolean();
    AtomicBoolean commitDone = new AtomicBoolean();
    AtomicBoolean rollbackDone = new AtomicBoolean();

    Flux<String> resourcePublisher = Flux.just("Resource", "Something Else")
        .doOnCancel(() -> {
          System.out.println("doOnCancel");
          cancelled.set(true);
        });

    Flux<String> test = Flux.usingWhen(
        resourcePublisher,
        (String tr) -> {
          System.out.println("closure " + tr);
          //return Flux.just(tr+" Supplied");
          return Flux.fromStream(tr.chars().mapToObj((int c)-> String.valueOf((char)c)));
        },
        (String tr) -> {
          System.out.println("async complete " + tr);
          return Mono.fromRunnable(() -> commitDone.set(true));
        },
        (String tr, Throwable err) -> {
          System.out.println("async error " + tr);
          return Mono.fromRunnable(() -> rollbackDone.set(true));
        },
        (String tr) -> {
          System.out.println("async cancele " + tr);
          return Mono.fromRunnable(() -> rollbackDone.set(true));
        }
    );

    StepVerifier.create(test)
        .expectNext("R")
        .expectNext("e")
        .expectNext("s")
        .expectNext("o")
        .expectNext("u")
        .expectNext("r")
        .expectNext("c")
        .expectNext("e")
        .expectComplete()
        .verifyThenAssertThat().hasNotDroppedErrors();
    assertThat(commitDone).isTrue();
    assertThat(rollbackDone).isFalse();
    assertThat(cancelled).as("resource publisher was cancelled").isTrue();
  }

  @Test
  public void failToGenerateClosureAppliesRollback() {
    TestResource testResource = new TestResource();

    Flux<String> test = Flux.usingWhen(
        Mono.just(testResource),
        (TestResource tr) -> {
          throw new UnsupportedOperationException("boom");
        },
        TestResource::commit,
        TestResource::rollback,
        TestResource::cancel
    );

    StepVerifier.create(test)
        .verifyErrorSatisfies(e -> assertThat(e).hasMessage("boom"));

    testResource.commitProbe.assertWasNotSubscribed();
    testResource.cancelProbe.assertWasNotSubscribed();
    testResource.rollbackProbe.assertWasSubscribed();
  }

  @Test
  public void failToGenerateClosureAppliesRollback2() {
    TestResource testResource = new TestResource();

    Flux<String> test = Flux.usingWhen(
        Mono.just(testResource),
        (TestResource tr) -> Flux.just(tr.toString()),
        TestResource::commit,
        TestResource::rollback,
        TestResource::cancel
    ).doOnNext(tr -> {
      throw new UnsupportedOperationException("boom");
    });

    StepVerifier.create(test)
        .verifyErrorSatisfies(e -> assertThat(e).hasMessage("boom"));

    testResource.commitProbe.assertWasNotSubscribed();
    testResource.cancelProbe.assertWasSubscribed();
    testResource.rollbackProbe.assertWasNotSubscribed();
  }
}
