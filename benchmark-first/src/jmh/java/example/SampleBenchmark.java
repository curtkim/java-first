package example;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class SampleBenchmark {
  @Benchmark
  public void fibClassic(Blackhole bh) {
    bh.consume(Fib.fibClassic(30));
  }

  @Benchmark
  public void fibTailRec(Blackhole bh) {
    bh.consume(Fib.tailRecFib(30));
  }
}
