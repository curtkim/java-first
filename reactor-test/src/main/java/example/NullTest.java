package example;

import reactor.core.publisher.Flux;

import java.util.Optional;

public class NullTest {

  public static void main(String[] args) {
    Flux<Optional<Long>> flux = Flux.just(Optional.of(4l), Optional.empty(), Optional.of(5l));
    flux.subscribe(v -> System.out.println(v));
  }
}
