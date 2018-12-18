package tutorial;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;

@EmbeddedKafka(topics = KafkaListenerBean.TOPIC, controlledShutdown = true)
@SpringBootTest
@TestPropertySource(properties = {
    "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}",
    "spring.kafka.consumer.auto-offset-reset=earliest"
})
public class KafkaListenerBeanTest {

  @Configuration
  @Import({AppConfiguration.class})
  public static class TestConfig {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Bean
    KafkaTemplate kafkaTemplate(){
      Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafka);
      ProducerFactory<byte[], String> producerFactory = new DefaultKafkaProducerFactory<>(producerProps);
      return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    Dao dao(){
      return new DummayDao();
    }
  }

  @Autowired
  private DummayDao dao;

  @Autowired
  ApplicationContext applicationContext;

  @Autowired
  KafkaTemplate<byte[], String> kafkaTemplate;

  @Test
  public void test() throws InterruptedException {
    /*
    System.err.println(System.getProperty("java.io.tmpdir"));
    System.out.println(embeddedKafka.getBrokersAsString());
    System.out.println(embeddedKafka.getTopics());
    System.out.println(embeddedKafka.getZookeeperConnectionString());
    System.out.println(applicationContext.getEnvironment().getProperty("spring.kafka.consumer.bootstrap-servers"));
    System.out.println(applicationContext.getEnvironment().getProperty("spring.kafka.consumer.auto-offset-reset"));
    */
    System.out.println("=====");
    for(String name : applicationContext.getBeanDefinitionNames())
      System.out.println(name);
    System.out.println("=====");

    kafkaTemplate.send(KafkaListenerBean.TOPIC, "123");
    Thread.currentThread().sleep(1*1000);
    assertEquals("123", dao.buffer.get(0));
  }
}

class DummayDao implements Dao {

  List<String> buffer = new ArrayList<>();

  @Override
  public void add(String item) {
    buffer.add(item);
  }

  public List<String> getBuffer() {
    return buffer;
  }
}
