package netty;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.io.IOException;
import java.util.function.BiFunction;

public class HandleServer {
    public static void main(String[] args) throws InterruptedException, IOException {

        BiFunction<HttpServerRequest, HttpServerResponse, Publisher<Void>> handle =
                (req, res) -> res.sendString(Flux.just("hello"));

        HttpServer.create()
                .host("0.0.0.0")
                .port(8080)
                .handle(handle)
                .bind()
                .block();

        System.in.read();
    }
}
