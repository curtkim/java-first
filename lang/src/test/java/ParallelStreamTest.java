import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParallelStreamTest {
  @Test
  public void giveRangeOfLongs_whenSummedInParallel_shouldBeEqualToExpectedTotal()
      throws InterruptedException, ExecutionException {

    long firstNum = 1;
    long lastNum = 1_000_000;

    List<Long> aList = LongStream.rangeClosed(firstNum, lastNum).boxed()
        .collect(Collectors.toList());

    ForkJoinPool customThreadPool = new ForkJoinPool(4);
    long actualTotal = customThreadPool.submit(
        () -> aList.parallelStream().reduce(0L, Long::sum)).get();

    assertEquals((lastNum + firstNum) * lastNum / 2, actualTotal);
  }

  @Test
  public void test(){
    List<Long> aList = LongStream.rangeClosed(1, 10).boxed()
        .collect(Collectors.toList());

    ForkJoinPool customThreadPool = new ForkJoinPool(1);
    ForkJoinTask task = customThreadPool.submit(
        () -> aList.parallelStream().forEach(it -> System.out.println(Thread.currentThread().getName() + "" +it)));

    task.join();
    aList.parallelStream().forEach(it -> System.out.println(Thread.currentThread().getName() + "" +it));
  }
}
