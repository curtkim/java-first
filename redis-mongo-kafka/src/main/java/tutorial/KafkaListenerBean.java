package tutorial;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tutorial.domain.Pet;

@Component
public class KafkaListenerBean {

  public static final String TOPIC = "pets";

  @Autowired
  ObjectMapper objectMapper;

  @KafkaListener(topics = TOPIC)
  public void listen(String body){
    System.out.println(body);
    try {
      Pet pet = objectMapper.readValue(body, Pet.class);
      System.out.println(pet);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
