package org.springframework.samples.web.reactive.function;

public class ServerMain {

  public static void main(String[] args) throws Exception {
    Server server = new Server();
    server.startReactorServer();
//		server.startTomcatServer();

    System.out.println("Press ENTER to exit.");
    System.in.read();
  }
}
