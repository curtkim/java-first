package example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;


public class ClientMain {
  private static final Logger LOGGER = LoggerFactory.getLogger(ClientMain.class);

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    WebSocketClient webSocketClient = new StandardWebSocketClient();


    WebSocketSession webSocketSession = webSocketClient.doHandshake(
        new TextWebSocketHandler() {
          @Override
          public void handleTextMessage(WebSocketSession session, TextMessage message) {
            LOGGER.info("received message - " + message.getPayload());
          }
          @Override
          public void afterConnectionEstablished(WebSocketSession session) {
            LOGGER.info("established connection - " + session);
          }
        }, new WebSocketHttpHeaders(), URI.create("ws://localhost:8080/echo")
    ).get();

    newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
      try {
        TextMessage message = new TextMessage("Hello !!");
        webSocketSession.sendMessage(message);
        LOGGER.info("sent message - " + message.getPayload());
      } catch (Exception e) {
        LOGGER.error("Exception while sending a message", e);
      }
    }, 1, 10, TimeUnit.SECONDS);
  }
}
