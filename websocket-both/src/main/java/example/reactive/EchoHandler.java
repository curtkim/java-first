package example.reactive;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class EchoHandler implements WebSocketHandler
{
  @Override
  public Mono<Void> handle(WebSocketSession session)
  {
    Flux<WebSocketMessage> output = session.receive()
        .switchMap(message -> {
          String body = message.getPayloadAsText();
          System.out.println("receive from client" + body);
          return Flux.interval(Duration.ofSeconds(1)).map(it -> body);
        })
        .map(value -> session.textMessage("Echo " + value));

    return session.send(output);
  }
}