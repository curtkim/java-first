package example;

import java.net.URI;
import java.time.Duration;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;

import reactor.core.publisher.Mono;


public class ClientMain {
  public static void main(String[] args) throws InterruptedException {

    WebSocketClient client = new ReactorNettyWebSocketClient();

    WebSocketHandler handler = session -> {
      return session.send(
          Mono.just(session.textMessage("event-spring-reactive-client-websocket"))
      ).thenMany(
          session.receive().doOnNext(msg -> {
            System.out.println("--> receive from server" + msg.getPayloadAsText());
          })
      )
      .then();
    };

    client.execute(URI.create("ws://localhost:8080/echo"), handler)
    .block(Duration.ofSeconds(10L));
  }
}
