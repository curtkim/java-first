package com.example.rediskryo;

import com.esotericsoftware.kryo.Kryo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

  Kryo makeKryo(){
    Kryo kryo = new Kryo();
    kryo.register(Person.class);
    return kryo;
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory){
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setValueSerializer(new KryoRedisSerializer<>(makeKryo()));
    template.afterPropertiesSet();
    return template;
  }

}
