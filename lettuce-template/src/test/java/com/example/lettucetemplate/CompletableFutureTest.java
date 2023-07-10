package com.example.lettucetemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CompletableFutureTest {
  public static void main(String[] args) throws Exception {

    // Initialize first completable future (takes 2 seconds)
    final CompletableFuture<String> first = CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(2_000);
        return "foo";
      } catch(Exception e) {
        throw new RuntimeException(e);
      }
    });

    // Initialize second completable-future (takes 3 seconds)
    final CompletableFuture<String> second = first.thenApply(firstResult -> {
      try {
        Thread.sleep(1_000);
        return firstResult + "bar";
      } catch(Exception e) {
        throw new RuntimeException(e);
      }
    });

    // The second completable future should take three seconds total,
    // but really, it only takes one second total (due to line 31).
    //
    // Try changing the timeout to `500`.
    final String firstResult = first.join();
    final String secondResult = second.get(1500, TimeUnit.MILLISECONDS);
    System.out.println(firstResult);
    System.out.println(secondResult);
  }
}