package example.plain;

//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//
////@Configuration
////@EnableWebSocket
//public class PlainConfig implements WebSocketConfigurer {
//
//  @Bean
//  EchoWebSocketHandler echoWebSocketHandler() {
//    return new EchoWebSocketHandler();
//  }
//
//  @Override
//  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//    registry.addHandler(echoWebSocketHandler(),"/echo_plain");
//  }
//}