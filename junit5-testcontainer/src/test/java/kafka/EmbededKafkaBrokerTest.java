package kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import java.util.HashMap;
import java.util.Map;

public class EmbededKafkaBrokerTest {

  private static String SENDER_TOPIC = "foo.t";

  private static EmbeddedKafkaBroker kafkaBroker;

  private static KafkaTemplate<String , String> kafkaTemplate;

  @BeforeAll
  public static void setUp() {
    long startTime = System.currentTimeMillis();
    System.out.println("setup");
    kafkaBroker  = new EmbeddedKafkaBroker(1, true, SENDER_TOPIC);

    System.out.println("after broker " + (System.currentTimeMillis()- startTime));

    Map<String, Object> configProps = new HashMap<>();
    configProps.put(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
        kafkaBroker.getBrokersAsString());
    configProps.put(
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        StringSerializer.class);
    configProps.put(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        StringSerializer.class);

    kafkaTemplate = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configProps));
  }

  @AfterAll
  public static void tearDown(){
  }

  @Test
  public void test(){
    System.out.println("start test");
    // 여기서 timeout이 발생한다.
    kafkaTemplate.send(SENDER_TOPIC, "a", "1");

  }

  /*
  @Autowired
  private Sender sender;

  private KafkaMessageListenerContainer<String, String> container;
  private BlockingQueue<ConsumerRecord<String, String>> records;

  public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, SENDER_TOPIC);


  @Before
  public void setUp() throws Exception {
    // set up the Kafka consumer properties
    Map<String, Object> consumerProperties =
        KafkaTestUtils.consumerProps("sender", "false", embeddedKafka);

    // create a Kafka consumer factory
    DefaultKafkaConsumerFactory<String, String> consumerFactory =
        new DefaultKafkaConsumerFactory<String, String>(consumerProperties);

    // set the topic that needs to be consumed
    ContainerProperties containerProperties = new ContainerProperties(SENDER_TOPIC);

    // create a Kafka MessageListenerContainer
    container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);

    // create a thread safe queue to store the received message
    records = new LinkedBlockingQueue<>();

    // setup a Kafka message listener
    container.setupMessageListener((MessageListener<String, String>) record -> {
      LOGGER.debug("test-listener received message='{}'", record.toString());
      records.add(record);
    });

    // start the container and underlying message listener
    container.start();

    // wait until the container has the required number of assigned partitions
    ContainerTestUtils.waitForAssignment(container, embeddedKafka.getPartitionsPerTopic());
  }

  @After
  public void tearDown() {
    // stop the container
    container.stop();
  }

  @Test
  public void testSend() throws InterruptedException {
    // send the message
    String greeting = "Hello Spring Kafka Sender!";
    sender.send(greeting);

    // check that the message was received
    ConsumerRecord<String, String> received = records.poll(10, TimeUnit.SECONDS);

    // Hamcrest Matchers to check the value
    assertThat(received, hasValue(greeting));

    // AssertJ Condition to check the key
    assertThat(received).has(key(null));
  }
  */
}
