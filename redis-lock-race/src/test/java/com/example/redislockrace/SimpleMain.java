package com.example.redislockrace;

import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.ArrayList;


public class SimpleMain {
  // return (id, data, response_string) tuple flux
  static Flux<Tuple3<String, Long, String>> makeRequestSeq(String id, int count, long interval, WebClient client) {

    Flux<Tuple2<String, Long>> flux = Flux.generate(() -> 1L, (state, sink) -> {
      if (state == 1) {
        sink.next(Tuples.of("begin", state));
      } else if (state == count) {
        sink.next(Tuples.of("end", state));
      } else if (state > count){
        sink.next(Tuples.of("append", state));
        sink.complete();
      } else {
        sink.next(Tuples.of("append", state));
      }
      return state + 1;
    });

    return flux
        .delayElements(Duration.ofMillis(interval))
        //.doOnNext((tuple) -> System.out.println(tuple))
        .flatMap((tuple) ->
            client.get()
                .uri(uriBuilder ->
                    uriBuilder
                        .path(String.format("/%s/%s", id, tuple.getT1()))
                        .queryParam("value", tuple.getT2())
                        .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .map((body) -> Tuples.of(id, tuple.getT2(), body))
        )
        .concatWith(
            client.get().uri("/" + id)
                .retrieve()
                .bodyToMono(String.class)
                .map((body) -> Tuples.of(id, (long) count, body))
        );
  }

  public static void main(String[] args) {
    ConnectionProvider connectionProvider = ConnectionProvider.builder("myConnectionPool")
        .maxConnections(10)
        .pendingAcquireMaxCount(8000)
        .build();
    ReactorClientHttpConnector clientHttpConnector = new ReactorClientHttpConnector(HttpClient.create(connectionProvider));
    WebClient client = WebClient.builder()
        .clientConnector(clientHttpConnector)
        .baseUrl("http://localhost:8080")
        .build();

    LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
    lettuceConnectionFactory.afterPropertiesSet();
    lettuceConnectionFactory.getConnection().flushDb();

    ArrayList<Flux<Tuple3<String, Long, String>>> streams = new ArrayList<>();
    for(int i =0; i < 10; i++)
      streams.add( makeRequestSeq(i+"", 100, 30, client));

    Flux.merge(streams)
        .filter((tuple)-> !tuple.getT3().equals("ok")) //&& tuple.getT3().startsWith("false"))
        .doOnNext(System.out::println)
        .collectList()
        .block();
  }
}
