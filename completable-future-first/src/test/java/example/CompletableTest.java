package example;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompletableTest {

  @Test
  public void already_know_result() throws ExecutionException, InterruptedException {
    Future<String> completableFuture = CompletableFuture.completedFuture("Hello");
    assertEquals("Hello", completableFuture.get());
  }

  public Future<String> calculateAsync() {
    CompletableFuture<String> completableFuture = new CompletableFuture<>();

    Executors.newCachedThreadPool().submit(() -> {
      Thread.sleep(500);
      completableFuture.complete("Hello");
      return null;
    });
    return completableFuture;
  }

  @Test
  public void when_complete() throws InterruptedException, ExecutionException {
    assertEquals("Hello", calculateAsync().get());
  }

  @Test
  @DisplayName("supplyAsync")
  public void encapsulate_logic() throws ExecutionException, InterruptedException {
    CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello");
    assertEquals("Hello", future.get());
  }

  @Test
  @DisplayName("thenApply")
  public void map() throws ExecutionException, InterruptedException {
    CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");
    CompletableFuture<String> future = completableFuture.thenApply(s -> s + " World");
    assertEquals("Hello World", future.get());
  }

  @Test
  @DisplayName("thenAccept")
  public void map2() throws ExecutionException, InterruptedException {
    CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");
    CompletableFuture<Void> future = completableFuture.thenAccept(s -> System.out.println("Computation returned: " + s));
    future.get();
  }

  @Test
  @DisplayName("thenRun")
  public void map3() throws ExecutionException, InterruptedException {
    CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");
    CompletableFuture<Void> future = completableFuture.thenRun(() -> System.out.println("Computation finished."));
    future.get();
  }

  @Test
  @DisplayName("thenCompose(flatMap) method to chain two Futures sequentially")
  public void compose() throws ExecutionException, InterruptedException {
    CompletableFuture<String> completableFuture =
        CompletableFuture.supplyAsync(() -> "Hello")
            .thenCompose((String s) -> CompletableFuture.supplyAsync(() -> s + " World"));
    assertEquals("Hello World", completableFuture.get());
  }

  @Test
  public void thenCombine() throws ExecutionException, InterruptedException {
    CompletableFuture<String> completableFuture =
        CompletableFuture.supplyAsync(() -> "Hello")
            .thenCombine(CompletableFuture.supplyAsync(() -> " World"), (s1, s2) -> s1 + s2);
    assertEquals("Hello World", completableFuture.get());
  }

  @DisplayName("thenApply는 map, thenCompose는 flatMap")
  public void thenApply_vs_thenCompose(){
    /*
    CompletableFuture<Integer> compute = CompletableFuture.supplyAsync(() -> 1);
    CompletableFuture<Integer> finalResult = compute.thenApply(s-> s + 1);

    CompletableFuture<Integer> computeAnother(Integer i){
      return CompletableFuture.supplyAsync(() -> 10 + i);
    }
    CompletableFuture<Integer> finalResult = compute().thenCompose(this::computeAnother);
    */
  }


  CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
  CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "Beautiful");
  CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> "World");
  @Test
  public void allOf() throws ExecutionException, InterruptedException {

    CompletableFuture<Void> combinedFuture
        = CompletableFuture.allOf(future1, future2, future3);

    combinedFuture.get();

    assertTrue(future1.isDone());
    assertTrue(future2.isDone());
    assertTrue(future3.isDone());

  }

  @Test
  public void join(){
    String combined = Stream.of(future1, future2, future3)
        .map(CompletableFuture::join)
        .collect(Collectors.joining(" "));
    assertEquals("Hello Beautiful World", combined);
  }

  @Test
  public void async() throws ExecutionException, InterruptedException {
    CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
      System.out.println(Thread.currentThread().getName());
      return "Hello";
    });
    CompletableFuture<String> future = completableFuture.thenApplyAsync(s -> {
      System.out.println(Thread.currentThread().getName());
      return s + " World";
    });
    assertEquals("Hello World", future.get());
  }

}
