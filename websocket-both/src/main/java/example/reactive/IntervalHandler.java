package example.reactive;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class IntervalHandler implements WebSocketHandler {

  @Override
  public Mono<Void> handle(WebSocketSession session) {
    return session.send(
        Flux.interval(Duration.ofSeconds(1))
            .map(it -> it.toString())
            .map(session::textMessage)
    );
  }
}
