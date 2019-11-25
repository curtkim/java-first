package netty;

import reactor.core.publisher.Flux;
import reactor.netty.ByteBufFlux;
import reactor.netty.http.client.HttpClient;

public class PostClient {
    public static void main(String[] args) {
        String response =
                HttpClient.create()          // Prepares a HTTP client for configuration.
                        .port(8080)          // Obtains the server's port and provides it as a port to which this
                        .wiretap(true)       // Applies a wire logger configuration.
                        .headers(h -> h.add("Content-Type", "text/plain")) // Adds headers to the HTTP request
                        .post()              // Specifies that POST method will be used
                        .uri("/test/World")  // Specifies the path
                        .send(ByteBufFlux.fromString(Flux.just("Hello")))  // Sends the request body
                        .responseContent()   // Receives the response body
                        .aggregate()
                        .asString()
                        .log("http-client")
                        .block();

        System.out.println(response);

    }
}
