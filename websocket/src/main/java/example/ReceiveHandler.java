package example;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class ReceiveHandler implements WebSocketHandler {
    @Override
    public Mono<Void> handle(WebSocketSession session) {

        System.out.println("start " + session.getHandshakeInfo().getUri().getPath());

        session.receive().subscribe(
                (message) -> {
                    System.out.println(message.getPayloadAsText());
                },
                (err) -> {
                },
                () -> {
                    System.out.println("closed");
                }
        );

        // for health check
        return session.send(
                Flux.interval(Duration.ofSeconds(3))
                        .map(it -> "ok")
                        .map(session::textMessage));
    }
}
