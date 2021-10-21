package example;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class HelloServer {

  public static void main(String[] args) throws InterruptedException, IOException {
    Server server = ServerBuilder.forPort(8080)
        .addService(new HelloWorldServiceImpl())
        .build()
        .start();
    server.awaitTermination();
  }
}
