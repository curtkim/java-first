package com.example.rediskryo;

import com.esotericsoftware.kryo.Kryo;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.serializer.RedisSerializer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KryoRedisSerializerTest {

  @Test
  public void test(){
    Kryo kryo = new Kryo();
    kryo.register(Person.class);

    RedisSerializer redisSerializer = new KryoRedisSerializer<Person>(kryo);
    byte[] bytes = redisSerializer.serialize(new Person("kim", 40));

    Person p2 = (Person)redisSerializer.deserialize(bytes);
    assertEquals("kim", p2.name);
  }
}
