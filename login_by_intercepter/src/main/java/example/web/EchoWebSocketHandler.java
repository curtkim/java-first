package example.web;

import example.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


public class EchoWebSocketHandler extends TextWebSocketHandler {

  private static final Logger logger = LoggerFactory.getLogger(EchoWebSocketHandler.class);

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    System.out.println(session.getAttributes());
    User user = (User) session.getAttributes().get("login");
    System.out.println(user);
    logger.info("접속 : {}",  session);
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    logger.info("메세지 전송 = {} : {}",session,message.getPayload());
    TextMessage msg = new TextMessage(message.getPayload());
    session.sendMessage(msg);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    logger.info("퇴장 : {}",  session);
  }

}