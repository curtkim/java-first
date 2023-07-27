package org.example;

import com.tutorialspoint.theater.TheaterOuterClass;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Collections;
import java.util.Properties;

public class KafkaProtobufConsumer {

  public static void main(String[] args) throws Exception {
    Properties props = new Properties();

    // kafka server host 및 port 설정
    props.put("bootstrap.servers", Constants.BROKER);
    props.put("schema.registry.url", Constants.SCHEMA_REGISTRY);
    props.put("group.id", "karim-group-id-1"); // group-id 설정
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("value.deserializer", "io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializer");
    props.put("auto.offset.reset", "earliest");

    KafkaConsumer<String, TheaterOuterClass.Theater> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Collections.singletonList(Constants.TOPIC));

    try {
      while (true) {
        // 계속 loop를 돌면서 producer의 message를 띄운다.
        ConsumerRecords<String, TheaterOuterClass.Theater> records = consumer.poll(100);
        for (ConsumerRecord<String, TheaterOuterClass.Theater> record : records) {
          System.out.println(String.format("offset = %d", record.offset()));
          System.out.println(record.value());
        }
      }
    } catch (Exception e) {
    } finally {
      consumer.close();
    }
  }
}
