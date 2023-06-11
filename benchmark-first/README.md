## howto

    ./gradlew jmh


## 설정
- fork
- warmupIterations
- warmup
- iterations
- timeOnIteration
 

## 구조

    benchmark1
        fork1
            Warmup Iteration    1
            Iteration           1

## 소요시간 

    jmh {
        fork = 1
        warmupIterations = 1
        iterations = 10
        timeOnIteration = '1s'  // Time to spend at each measurement iteration.
        warmup = '1s'
    }
    // 2개의 benchmark를 
    // 1초 warmup, 1초 10회 iteration으로 
    // 총 22초 = 2*(1 + 10*1) = 걸린다.


    jmh {
        fork = 2
        warmupIterations = 2
        warmup = '2s'
        iterations = 3
        timeOnIteration = '2s'
    }
    // 2개의 benchmark를 
    // 2초 warmup 2번 , 2초 iteration 3번
    // 총 40초 = 2*2*(2*2+3*2) 소요


## results log

    # JMH version: 1.35
    # VM version: JDK 11.0.15, OpenJDK 64-Bit Server VM, 11.0.15+10-Ubuntu-0ubuntu0.20.04.1
    # VM invoker: /usr/lib/jvm/java-11-openjdk-amd64/bin/java
    # VM options: -Dfile.encoding=UTF-8 -Djava.io.tmpdir=/home/curt/projects/spring-first/benchmark-first/build/tmp/jmh -Duser.country=US -Duser.language=en -Duser.variant
    # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
    # Warmup: 5 iterations, 10 s each
    # Measurement: 5 iterations, 10 s each
    # Timeout: 10 min per iteration
    # Threads: 1 thread, will synchronize iterations
    # Benchmark mode: Throughput, ops/time
    # Benchmark: example.SampleBenchmark.fibClassic

    # Run progress: 0.00% complete, ETA 00:16:40
    # Fork: 1 of 5
    # Warmup Iteration   1: 282.375 ops/s
    # Warmup Iteration   2: 282.444 ops/s
    # Warmup Iteration   4: 283.552 ops/s
    # Warmup Iteration   5: 284.813 ops/s
    Iteration   1: 283.805 ops/sG [55s]
    Iteration   2: 283.391 ops/sG [1m 5s]
    Iteration   3: 283.148 ops/sG [1m 15s]
    Iteration   4: 284.884 ops/sG [1m 25s]
    Iteration   5: 283.883 ops/sG [1m 35s]

    # Run progress: 10.00% complete, ETA 00:15:02
    # Fork: 2 of 5
    # Warmup Iteration   1: 297.330 ops/s]
    # Warmup Iteration   2: 297.561 ops/s]
    # Warmup Iteration   3: 296.376 ops/s
    # Warmup Iteration   5: 298.092 ops/s]
    Iteration   1: 296.850 ops/sG [2m 35s]
    Iteration   2: 297.936 ops/sG [2m 45s]
    Iteration   3: 298.702 ops/sG [2m 55s]
    Iteration   4: 292.331 ops/sG [3m 5s]
    Iteration   5: 291.211 ops/sG [3m 15s]

    # Run progress: 20.00% complete, ETA 00:13:22
    # Fork: 3 of 5
    # Warmup Iteration   1: 272.416 ops/s]
    # Warmup Iteration   2: 284.147 ops/s]
    # Warmup Iteration   3: 282.201 ops/s]
    # Warmup Iteration   4: 282.466 ops/s]
    # Warmup Iteration   5: 282.925 ops/s
    Iteration   1: 284.090 ops/sG [4m 15s]
    Iteration   2: 280.381 ops/sG [4m 25s]
    Iteration   3: 284.215 ops/sG [4m 35s]
    Iteration   4: 281.810 ops/sG [4m 45s]
    Iteration   5: 276.267 ops/sG [4m 55s]

    # Run progress: 30.00% complete, ETA 00:11:41
    # Fork: 4 of 5
    # Warmup Iteration   1: 284.388 ops/s
    # Warmup Iteration   2: 283.333 ops/s]
    # Warmup Iteration   3: 282.926 ops/s]
    # Warmup Iteration   4: 281.984 ops/s]
    # Warmup Iteration   5: 284.319 ops/s]
    Iteration   1: 283.872 ops/sG [5m 55s]
    Iteration   3: 283.221 ops/sG [6m 15s]
    Iteration   4: 280.696 ops/sG [6m 25s]
    Iteration   5: 280.495 ops/sG [6m 35s]

    # Run progress: 40.00% complete, ETA 00:10:01
    # Fork: 5 of 5
    # Warmup Iteration   1: 278.534 ops/s]
    # Warmup Iteration   2: 277.664 ops/s]
    # Warmup Iteration   3: 278.309 ops/s
    # Warmup Iteration   4: 282.652 ops/s]
    # Warmup Iteration   5: 284.044 ops/s]
    Iteration   1: 284.294 ops/sG [7m 36s]
    Iteration   2: 279.531 ops/sG [7m 46s]
    Iteration   3: 284.356 ops/sG [7m 56s]
    Iteration   4: 282.834 ops/sG [8m 6s]
    Iteration   5: 281.891 ops/sG [8m 16s]


    Result "example.SampleBenchmark.fibClassic":
    285.140 ±(99.9%) 4.309 ops/s [Average]
    (min, avg, max) = (276.267, 285.140, 298.702), stdev = 5.752
    CI (99.9%): [280.831, 289.449] (assumes normal distribution)


    # JMH version: 1.35
    # VM version: JDK 11.0.15, OpenJDK 64-Bit Server VM, 11.0.15+10-Ubuntu-0ubuntu0.20.04.1
    # VM invoker: /usr/lib/jvm/java-11-openjdk-amd64/bin/java
    # VM options: -Dfile.encoding=UTF-8 -Djava.io.tmpdir=/home/curt/projects/spring-first/benchmark-first/build/tmp/jmh -Duser.country=US -Duser.language=en -Duser.variant
    # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
    # Warmup: 5 iterations, 10 s each
    # Measurement: 5 iterations, 10 s each
    # Timeout: 10 min per iteration
    # Threads: 1 thread, will synchronize iterations
    # Benchmark mode: Throughput, ops/time
    # Benchmark: example.SampleBenchmark.fibTailRec

    # Run progress: 50.00% complete, ETA 00:08:21
    # Fork: 1 of 5
    # Warmup Iteration   1: 38154130.013 ops/s
    # Warmup Iteration   2: 37221430.349 ops/s
    # Warmup Iteration   3: 39276016.392 ops/s
    # Warmup Iteration   4: 39417853.400 ops/s
    # Warmup Iteration   5: 39324998.986 ops/s
    Iteration   1: 38679084.188 ops/s 16s]
    Iteration   2: 38844601.613 ops/s 26s]
    Iteration   3: 38866126.348 ops/s 36s]
    Iteration   4: 37372555.737 ops/s 46s]
    Iteration   5: 38139109.241 ops/s 56s]

    # Run progress: 60.00% complete, ETA 00:06:41
    # Fork: 2 of 5
    # Warmup Iteration   1: 38486224.938 ops/s
    # Warmup Iteration   2: 37862897.219 ops/s
    # Warmup Iteration   3: 39480256.609 ops/s
    # Warmup Iteration   4: 38952502.664 ops/s
    # Warmup Iteration   5: 39235799.439 ops/s
    Iteration   1: 38614118.613 ops/sm 56s]
    Iteration   2: 39076406.796 ops/sm 6s]
    Iteration   3: 38657400.686 ops/sm 16s]
    Iteration   4: 38653662.493 ops/sm 26s]
    Iteration   5: 38681154.325 ops/sm 36s]

    # Run progress: 70.00% complete, ETA 00:05:00
    # Fork: 3 of 5
    # Warmup Iteration   1: 38164577.176 ops/s
    # Warmup Iteration   2: 37911112.043 ops/s
    # Warmup Iteration   3: 39323715.163 ops/s
    # Warmup Iteration   4: 39202987.674 ops/s
    # Warmup Iteration   5: 39166247.303 ops/s
    Iteration   1: 39258443.074 ops/sm 36s]
    Iteration   2: 38973068.188 ops/sm 46s]
    Iteration   3: 39206901.375 ops/sm 56s]
    Iteration   4: 39057801.871 ops/sm 6s]
    Iteration   5: 38876615.723 ops/sm 16s]

    # Run progress: 80.00% complete, ETA 00:03:20
    # Fork: 4 of 5
    # Warmup Iteration   1: 38393360.584 ops/s
    # Warmup Iteration   2: 37292669.670 ops/s
    # Warmup Iteration   3: 39391858.772 ops/s
    # Warmup Iteration   4: 39230018.821 ops/s
    # Warmup Iteration   5: 39271327.890 ops/s
    Iteration   1: 38654874.944 ops/sm 17s]
    Iteration   2: 39186759.766 ops/sm 27s]
    Iteration   3: 39315164.320 ops/sm 37s]
    Iteration   4: 39294077.724 ops/sm 47s]
    Iteration   5: 38564455.001 ops/sm 57s]

    # Run progress: 90.00% complete, ETA 00:01:40
    # Fork: 5 of 5
    # Warmup Iteration   1: 38406738.017 ops/s
    # Warmup Iteration   2: 37910825.201 ops/s
    # Warmup Iteration   3: 39115729.169 ops/s
    # Warmup Iteration   4: 39174862.190 ops/s
    # Warmup Iteration   5: 39308138.463 ops/s
    Iteration   1: 38266150.129 ops/sm 57s]
    Iteration   2: 39068990.772 ops/sm 7s]
    Iteration   3: 338696657.715 ops/s 17s]
    Iteration   5: 39215898.216 ops/s6m 37s]


    Result "example.SampleBenchmark.fibTailRec":
    38799427.612 ±(99.9%) 321392.726 ops/s [Average]
    (min, avg, max) = (37372555.737, 38799427.612, 39315164.320), stdev = 429050.095
    CI (99.9%): [38478034.886, 39120820.339] (assumes normal distribution)


    # Run complete. Total time: 00:16:42

    REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
    why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
    experiments, perform baseline and negative tests that provide experimental control, make sure
    the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
    Do not assume the numbers tell you what you want them to tell.

    Benchmark                    Mode  Cnt         Score        Error  Units
    SampleBenchmark.fibClassic  thrpt   25       285.140 ±      4.309  ops/s
    SampleBenchmark.fibTailRec  thrpt   25  38799427.612 ± 321392.726  ops/s

    Benchmark result is saved to /home/curt/projects/spring-first/benchmark-first/build/results/jmh/results.txt