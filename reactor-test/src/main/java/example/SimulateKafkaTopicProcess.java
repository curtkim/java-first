package example;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class SimulateKafkaTopicProcess {

  public static void main(String[] args) {

    int count = 300;
    int partitions = 5;
    Scheduler parallelScheduler = Schedulers.newParallel("parallel", partitions);
    Scheduler elasticScheduler = Schedulers.newBoundedElastic(10, 1000, "elastic");

    long startTime = System.currentTimeMillis();
    List<Long> result = Flux.range(0, count)
        .groupBy(i -> i % partitions)
        .flatMap(groupFlux ->
            //groupFlux.map(SimulateKafkaTopicProcess::process)
            //groupFlux.publishOn(parallelScheduler).map(SimulateKafkaTopicProcess::process)
            //groupFlux.publishOn(elasticScheduler).map(SimulateKafkaTopicProcess::process)
            groupFlux.publishOn(parallelScheduler).flatMapSequential(SimulateKafkaTopicProcess::processAsFlux, 2)
        )
        //.doOnNext(SimulateKafkaTopicProcess::print)
        .collectList().block();

    System.out.println("time=" + (System.currentTimeMillis() - startTime));
    parallelScheduler.dispose();
    elasticScheduler.dispose();

    Map<Long, List<Long>> groupByResults = result.stream().collect(Collectors.groupingBy(it -> it % partitions));
    for (Long partition : groupByResults.keySet()) {
      assert groupByResults.get(result).size() == count / partition;
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
    return Flux.just(process(value));
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
