package tutorial;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListenerBean {

  public static final String TOPIC = "test-topic";

  @Autowired
  Dao dao;

  @KafkaListener(topics = TOPIC, groupId = "tutorial")
  public void listen(String body) {
    dao.add(body);
  }

}
