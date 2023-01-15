package old;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;

public class Tutorial {

  @Test
  public void empty() {
    Mono<String> emptyMono = Mono.empty();
    StepVerifier.create(emptyMono).verifyComplete();
    Flux<String> emptyFlux = Flux.empty();
    StepVerifier.create(emptyFlux).verifyComplete();
  }

  @Test
  public void initialized() {
    Mono<String> nonEmptyMono = Mono.just("Joel");
    StepVerifier.create(nonEmptyMono)
            .expectNext("Joel")
            .verifyComplete();

    Flux<String> nonEmptyFlux = Flux.just("John", "Mike", "Sarah");
    StepVerifier.create(nonEmptyFlux)
            .expectNext("John", "Mike", "Sarah")
            .verifyComplete();

    Flux<String> fluxFromIterable = Flux.fromIterable(Arrays.asList("Tom", "Hardy", "Bane"));
    StepVerifier.create(fluxFromIterable)
            .expectNext("Tom", "Hardy", "Bane")
            .verifyComplete();
  }

  @Test
  public void zipping() {
    Flux<String> titles = Flux.just("Mr.", "Mrs.");
    Flux<String> firstNames = Flux.just("John", "Jane");
    Flux<String> lastNames = Flux.just("Doe", "Blake");
    Flux<String> names = Flux
        .zip(titles, firstNames, lastNames)
        .map(t -> t.getT1() + " " + t.getT2() + " " + t.getT3());
    StepVerifier.create(names).expectNext("Mr. John Doe", "Mrs. Jane Blake").verifyComplete();
    Flux<Long> delay = Flux.interval(Duration.ofMillis(5));
    Flux<String> firstNamesWithDelay = firstNames.zipWith(delay, (s, l) -> s);
    Flux<String> namesWithDelay = Flux
        .zip(titles, firstNamesWithDelay, lastNames)
        .map(t -> t.getT1() + " " + t.getT2() + " " + t.getT3());
    StepVerifier.create(namesWithDelay).expectNext("Mr. John Doe", "Mrs. Jane Blake").verifyComplete();
  }
}
