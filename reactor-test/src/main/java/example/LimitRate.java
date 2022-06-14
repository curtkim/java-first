package example;

import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;

public class LimitRate {
  public static void main(String[] args) throws IOException, InterruptedException {
    // 명시적으로 50개를 요청, 50개 까지만 출력된다.
//    Flux.range(1, 100)
//        .log()
//        .subscribe(
//            System.out::println,  // subscribe
//            System.out::println,  // error handler
//            () -> {},             // onComplete
//            s -> s.request(50) // subscription request
//        );

    // 32개를 request하고 출력하고
    // 24개를 parallel-11 thread에서 요청하고 출력하고
    // 24개를 parallel-11 thread에서 요청하고 출력하고
//    Flux.range(1, 100)
//        .log()
//        .delayElements(Duration.ofMillis(10))
//        .subscribe(System.out::println);
//    Thread.sleep(500);

    // Once the 75% of data got drained/emitted, then it automatically requests to refill the amount
    // 8개를 요청하게 된다.
    Flux.range(1, 100)
        .log()
        .limitRate(10)
        .delayElements(Duration.ofMillis(100))
        .subscribe(System.out::println);
    Thread.sleep(5000);

  }
}
