import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.*;


public class KafkaSampleConsumer {

  private static final Logger log = LoggerFactory.getLogger(KafkaSampleConsumer.class.getName());

  private static final String BOOTSTRAP_SERVERS = "localhost:9092";
  private static final String TOPIC = "demo-topic";

  private final ReceiverOptions<Integer, String> receiverOptions;
  private final SimpleDateFormat dateFormat;

  public KafkaSampleConsumer(String bootstrapServers) {

    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.CLIENT_ID_CONFIG, "sample-consumer");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "sample-group");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    receiverOptions = ReceiverOptions.create(props);
    dateFormat = new SimpleDateFormat("HH:mm:ss:SSS z dd MMM yyyy");
  }

  public Disposable consumeMessages(String topic, CountDownLatch latch) {
    ReceiverOptions<Integer, String> options = receiverOptions.subscription(Collections.singleton(topic))
        .addAssignListener((Collection<ReceiverPartition> partitions) -> log.debug("onPartitionsAssigned {}", partitions))
        .addRevokeListener(partitions -> log.debug("onPartitionsRevoked {}", partitions));
    Flux<ReceiverRecord<Integer, String>> kafkaFlux = KafkaReceiver.create(options).receive();
    return kafkaFlux.subscribe(record -> {
      ReceiverOffset offset = record.receiverOffset();
      System.out.printf("Received message: topic-partition=%s offset=%d timestamp=%s key=%d value=%s\n",
          offset.topicPartition(),
          offset.offset(),
          dateFormat.format(new Date(record.timestamp())),
          record.key(),
          record.value());
      offset.acknowledge();
      latch.countDown();
    });
  }

  public static void main(String[] args) throws Exception {
    int count = 20;
    CountDownLatch latch = new CountDownLatch(count);
    KafkaSampleConsumer consumer = new KafkaSampleConsumer(BOOTSTRAP_SERVERS);
    Disposable disposable = consumer.consumeMessages(TOPIC, latch);
    latch.await(10, TimeUnit.SECONDS);
    disposable.dispose();
  }
}