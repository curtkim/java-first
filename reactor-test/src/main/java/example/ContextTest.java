package example;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ContextTest {
  public static void main(String[] args) {
    basic();
    multiContextWrite();
    nested();
  }

  private static void nested() {
    String key = "message";
    Mono<String> r = Mono.just("Hello")
        .flatMap( s -> Mono
            .deferContextual(ctxView -> Mono.just(s + " " + ctxView.get(key)))
        )
        .flatMap( s -> Mono
            .deferContextual(ctxView -> Mono.just(s + " " + ctxView.get(key)))
            .contextWrite(ctx -> ctx.put(key, "Reactor"))
        )
        .contextWrite(ctx -> ctx.put(key, "World"));

    StepVerifier.create(r)
        .expectNext("Hello World Reactor")
        .verifyComplete();
  }

  private static void multiContextWrite() {
    String key = "message";
    Mono<String> r = Mono
        .deferContextual(ctx -> Mono.just("Hello " + ctx.get(key)))
        .contextWrite(ctx -> ctx.put(key, "Reactor"))
        .flatMap( s -> Mono.deferContextual(ctx -> Mono.just(s + " " + ctx.get(key))))
        .contextWrite(ctx -> ctx.put(key, "World"));

    StepVerifier.create(r)
        .expectNext("Hello Reactor World")
        .verifyComplete();
  }

  private static void basic() {
    String key = "message";
    Mono<String> r = Mono.just("Hello")
        .flatMap(s -> Mono.deferContextual(ctx -> Mono.just(s + " " + ctx.get(key))))
        .contextWrite(ctx -> ctx.put(key, "World"));

    StepVerifier.create(r)
        .expectNext("Hello World")
        .verifyComplete();
  }
}
