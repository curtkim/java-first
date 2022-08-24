package example;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.List;

public class TrafficGenerateMain {

  public static void main(String[] args) throws InterruptedException {
    Scheduler myScheduler = Schedulers.newParallel("my", 2);

    Flux<Integer> jobs = Flux.range(0, 10).map(it-> 10-it);

    List<Tuple2<Integer, Integer>> list = jobs.log()
        //.limitRate(2)
        //.subscribeOn(myScheduler)
        .flatMap(it-> Mono.just(it).zipWith(doJob(it, myScheduler)), 2)
        .collectList()
        .block();

    System.out.println(list);
    myScheduler.dispose();
  }

  public static Mono<Integer> doJob(int req, Scheduler scheduler){
    return Flux.just(req).repeat().take(req).delayElements(Duration.ofMillis(10), scheduler).reduce((a,b)-> a+b);
  }
}
