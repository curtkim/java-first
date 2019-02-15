package example2;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

public class Client {
  public static void main(String[] args) {
    test1();
  }

  public static void test1() {
    WebClient webClient = WebClient.create("http://localhost:8080");

    long startTime = System.currentTimeMillis();
    Mono<List<String>> result = Flux.just(new Pair("John", 1000), new Pair("Mike", 1000), new Pair("Sarah", 700))
        .flatMapSequential( it -> {
          String url = String.format("/?msg=%s&delay=%d",it.name, it.delay);
          System.out.println(Thread.currentThread().getName() + " " + url);
          return webClient.get().uri(url).retrieve().bodyToMono(String.class).log();
        })
        //.publishOn(Schedulers.elastic())
        .collectList();

    System.out.println(result.block());
    System.out.println(System.currentTimeMillis() - startTime);
  }
}

class Pair {
  String name;
  long delay;

  public Pair(String name, long delay) {
    this.name = name;
    this.delay = delay;
  }
}
