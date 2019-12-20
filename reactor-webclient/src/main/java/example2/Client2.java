package example2;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Client2 {
  public static void main(String[] args) {

    long startTime = System.currentTimeMillis();

    int numOfTasks = 10;
    List<Integer> result = Flux.range(0, numOfTasks)
        .flatMapSequential(index ->
                Mono.fromCallable(() -> {
                  Thread.sleep(ThreadLocalRandom.current().nextLong(2000));
                  System.out.println(String.format("[%s] Run %d", Thread.currentThread().getName(), index));
                  return index;
                })
                .publishOn(Schedulers.elastic())
        , 2)
        .collectList()
        .block();

    System.out.println(result);
    System.out.println(System.currentTimeMillis() - startTime);
  }
}
