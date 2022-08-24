package example;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class DelayElementMain {

  public static void main(String[] args) throws InterruptedException {
    Flux.just(1,2,3).delayElements(Duration.ofMillis(1000)).subscribe(it-> {
      System.out.println(it+" " + Thread.currentThread().getName());
    });

    Thread.sleep(5000);
  }
}
