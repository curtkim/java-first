package example;

import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;
import reactor.test.publisher.PublisherProbe;
import reactor.util.annotation.Nullable;

import java.time.Duration;
import java.util.logging.Level;

public class TestResource {

  private static final Duration DELAY = Duration.ofMillis(100);

  final Level level;

  PublisherProbe<Integer> commitProbe = PublisherProbe.empty();
  PublisherProbe<Integer> rollbackProbe = PublisherProbe.empty();
  PublisherProbe<Integer> cancelProbe = PublisherProbe.empty();

  TestResource() {
    this.level = Level.FINE;
  }

  TestResource(Level level) {
    this.level = level;
  }

  public Flux<String> data() {
    return Flux.just("Transaction started");
  }

  public Flux<Integer> commit() {
    this.commitProbe = PublisherProbe.of(
        Flux.just(3, 2, 1)
            .log("commit method used", level, SignalType.ON_NEXT, SignalType.ON_COMPLETE));
    return commitProbe.flux();
  }

  public Flux<Integer> commitDelay() {
    this.commitProbe = PublisherProbe.of(
        Flux.just(3, 2, 1)
            .delayElements(DELAY)
            .log("commit method used", level, SignalType.ON_NEXT, SignalType.ON_COMPLETE));
    return commitProbe.flux();
  }

  public Flux<Integer> commitError() {
    this.commitProbe = PublisherProbe.of(
        Flux.just(3, 2, 1)
            .delayElements(DELAY)
            .map(i -> 100 / (i - 1)) //results in divide by 0
            .log("commit method used", level, SignalType.ON_NEXT, SignalType.ON_COMPLETE));
    return commitProbe.flux();
  }

  @Nullable
  public Flux<Integer> commitNull() {
    return null;
  }

  public Flux<Integer> rollback(Throwable error) {
    this.rollbackProbe = PublisherProbe.of(
        Flux.just(5, 4, 3, 2, 1)
            .log("rollback me thod used on: " + error, level, SignalType.ON_NEXT, SignalType.ON_COMPLETE));
    return rollbackProbe.flux();
  }

  public Flux<Integer> rollbackDelay(Throwable error) {
    this.rollbackProbe = PublisherProbe.of(
        Flux.just(5, 4, 3, 2, 1)
            .delayElements(DELAY)
            .log("rollback method used on: " + error, level, SignalType.ON_NEXT, SignalType.ON_COMPLETE));
    return rollbackProbe.flux();
  }

  public Flux<Integer> rollbackError(Throwable error) {
    this.rollbackProbe = PublisherProbe.of(
        Flux.just(5, 4, 3, 2, 1)
            .delayElements(DELAY)
            .map(i -> 100 / (i - 1)) //results in divide by 0
            .log("rollback method used on: " + error, level, SignalType.ON_NEXT, SignalType.ON_COMPLETE));
    return rollbackProbe.flux();
  }

  @Nullable
  public Flux<Integer> rollbackNull(Throwable error) {
    return null;
  }

  public Flux<Integer> cancel() {
    this.cancelProbe = PublisherProbe.of(
        Flux.just(5, 4, 3, 2, 1)
            .log("cancel method used", level, SignalType.ON_NEXT, SignalType.ON_COMPLETE));
    return cancelProbe.flux();
  }

  public Flux<Integer> cancelError() {
    this.cancelProbe = PublisherProbe.of(
        Flux.just(5, 4, 3, 2, 1)
            .delayElements(DELAY)
            .map(i -> 100 / (i - 1)) //results in divide by 0
            .log("cancel method used", level, SignalType.ON_NEXT, SignalType.ON_COMPLETE));
    return cancelProbe.flux();
  }

  @Nullable
  public Flux<Integer> cancelNull() {
    return null;
  }
}
