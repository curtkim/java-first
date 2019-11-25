package netty;

import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRoutes;

import java.io.IOException;

public class PostServer {

    static void myRoute(HttpServerRoutes routes){
        routes.post("/test/{param}", (req, res) ->
                res.sendString(req.receive()
                        .asString()
                        .map(s -> s + ' ' + req.param("param") + '!')
                        .log("http-server")));
    }

    public static void main(String[] args) throws IOException {
        HttpServer.create()   // Prepares a HTTP server for configuration.
                .host("0.0.0.0")
                .port(8080)    // Configures the port number as zero, this will let the system pick up
                .route(PostServer::myRoute)
                .wiretap(true)
                .bind()
                .block();

        System.in.read();
        // curl -i -X POST -d Hello http://localhost:8080/test/World
    }
}
