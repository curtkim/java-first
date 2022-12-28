package example;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class FlatmapSchedulerMain {

  public static void main(String[] args){
    Flux.range(1, 10)
        .flatMap(FlatmapSchedulerMain::convert, 5)
        .doOnNext(System.out::println)
        .collectList()
        .block();
  }

  public static Flux<Long> convert(long val){
    System.out.println(Thread.currentThread().getName() + " " + val);
    return Flux.just(val).delayElements(Duration.ofMillis(random()));
  }

  static long random(){
    return ThreadLocalRandom.current().nextInt(1, 100);
  }
}
