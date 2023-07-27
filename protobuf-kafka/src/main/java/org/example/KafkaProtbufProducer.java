package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import com.tutorialspoint.theater.TheaterOuterClass.Theater;
import com.tutorialspoint.theater.TheaterOuterClass.Theater.PAYMENT_SYSTEM;

public class KafkaProtbufProducer {
  public static void main(String[] args) throws Exception{
    Properties props = new Properties();
    props.put("bootstrap.servers", Constants.BROKER);
    props.put("clientid", "foo");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "io.confluent.kafka.serializers.protobuf.KafkaProtobufSerializer");
    props.put("schema.registry.url", Constants.SCHEMA_REGISTRY);
    props.put("auto.register.schemas", "true");

    Producer<String, Theater> producer = new KafkaProducer<>(props);
    producer.send(new ProducerRecord<>(Constants.TOPIC, "SilverScreen", getTheater())).get();
    System.out.println("Sent to Kafka: \n" + getTheater());
    producer.flush();
    producer.close();
  }

  public static Theater getTheater() {
    List<String> snacks = new ArrayList<>();
    snacks.add("Popcorn");
    snacks.add("Coke");
    snacks.add("Chips");
    snacks.add("Soda");

    Map<String, Integer> ticketPrice = new HashMap<>();
    ticketPrice.put("Avengers Endgame", 700);
    ticketPrice.put("Captain America", 200);
    ticketPrice.put("Wonder Woman 1984", 400);

    Theater theater = Theater.newBuilder()
        .setName("Silver Screener")
        .setAddress("212, Maple Street, LA, California")
        .setDriveIn(true)
        .setTotalCapacity(320)
        .setMobile(98234567189L)
        .setBaseTicketPrice(22.45f)
        .setPayment(PAYMENT_SYSTEM.CREDIT_CARD)
        .putAllMovieTicketPrice(ticketPrice)
        .addAllSnacks(snacks)
        .build();
    return theater;
  }
}
