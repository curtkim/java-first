package tutorial;

import org.junit.jupiter.api.Test;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;


import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Collections;
import java.util.Map;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;


@EmbeddedKafka
@ExtendWith(SpringExtension.class)
public class SimpleKafkaTest {

  private static final String TOPIC = "testEmbedded";
  private static final String GROUP_NAME = "embeddedKafkaApplication";

  @Autowired
  private EmbeddedKafkaBroker embeddedKafka;

  @Test
  public void testSendReceive() {
    Map<String, Object> senderProps = KafkaTestUtils.producerProps(embeddedKafka);
    senderProps.put("key.serializer", ByteArraySerializer.class);
    senderProps.put("value.serializer", ByteArraySerializer.class);
    DefaultKafkaProducerFactory<byte[], byte[]> pf = new DefaultKafkaProducerFactory<>(senderProps);
    KafkaTemplate<byte[], byte[]> template = new KafkaTemplate<>(pf, true);
    template.setDefaultTopic(TOPIC);
    template.sendDefault("foo".getBytes());

    Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(GROUP_NAME, "false", embeddedKafka);
    consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    consumerProps.put("key.deserializer", ByteArrayDeserializer.class);
    consumerProps.put("value.deserializer", ByteArrayDeserializer.class);
    DefaultKafkaConsumerFactory<byte[], byte[]> cf = new DefaultKafkaConsumerFactory<>(consumerProps);

    Consumer<byte[], byte[]> consumer = cf.createConsumer();
    consumer.subscribe(Collections.singleton(TOPIC));
    ConsumerRecords<byte[], byte[]> records = consumer.poll(10_000);
    consumer.commitSync();

    assertThat(records.count()).isEqualTo(1);
    assertThat(new String(records.iterator().next().value())).isEqualTo("foo");
  }

}
