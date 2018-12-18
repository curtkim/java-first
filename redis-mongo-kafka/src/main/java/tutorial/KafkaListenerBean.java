package tutorial;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListenerBean {

  public static final String TOPIC = "pets";

  @KafkaListener(topics = TOPIC)
  public void listen(String body){
    System.out.println(body);
  }
}
