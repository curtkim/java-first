package example;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class SimulateKafkaTopicProcess {

  public static void main(String[] args) {

    int count = 300;
    int partitions = 10;
    Scheduler parallelScheduler = Schedulers.newParallel("parallel", partitions);

    long startTime = System.currentTimeMillis();
    List<Long> result = Flux.range(0, count)
        .groupBy(i -> i % partitions)
        .flatMap(groupFlux ->
            //groupFlux.map(SimulateKafkaTopicProcess::process)                                     // 16675ms 300*50
            //groupFlux.publishOn(parallelScheduler).map(SimulateKafkaTopicProcess::process)      // 1957ms  300*50/10
            //groupFlux.concatMap(SimulateKafkaTopicProcess::processAsFlux)                         // 1877ms
            //groupFlux.flatMapSequential(SimulateKafkaTopicProcess::processAsFlux)               // 184ms
            //groupFlux.flatMapSequential(SimulateKafkaTopicProcess::processAsFlux, 2)            // 1115ms
            groupFlux.flatMapSequential(SimulateKafkaTopicProcess::processAsFlux, 5)  // 588ms
        )
        //.doOnNext(SimulateKafkaTopicProcess::print)
        .collectList()
        .block();


    System.out.println("time=" + (System.currentTimeMillis() - startTime));
    parallelScheduler.dispose();

    System.out.println(result.stream().map(it -> it.toString()).collect(Collectors.joining(",")));

    assert result.size() == count;
    Map<Long, List<Long>> groupByResults = result.stream().collect(Collectors.groupingBy(it -> it % partitions));
    for (Long partition : groupByResults.keySet()) {
      assert groupByResults.get(partition).size() == count / partitions;
      assert isSorted(groupByResults.get(partition));
    }
    System.out.println("success");
  }

  static boolean isSorted(List<Long> list) {
    for (int i = 0; i < list.size() - 1; i++) {
      if (list.get(i) > list.get(i + 1))
        return false;
    }
    return true;
  }

  public static void print(long value) {
    System.out.println(value + " ");
  }

  public static Flux<Long> processAsFlux(long value){
    return Flux.just(value).delayElements(Duration.ofMillis(randomLong()));
    // 명시적으로 Thread.sleep()호출 하는 것보다, delayElements를 호출하면 더 효율적이다.
  }

  static long randomLong(){
    return ThreadLocalRandom.current().nextInt(1, 100);
  }

  public static long process(long value) {
    try {
      Thread.sleep(ThreadLocalRandom.current().nextInt(1, 100));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    System.out.println(Thread.currentThread().getName() + " " + value);
    return value * 1;
  }
}
