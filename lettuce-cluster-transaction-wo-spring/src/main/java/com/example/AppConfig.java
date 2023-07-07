package com.example;

import com.example.domain.MyService;
import com.example.domain.SliceDao;
import com.example.domain.SummaryDao;
import com.example.lettuce.data.LettuceTemplate;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class AppConfig {

  @Bean(destroyMethod = "close")
  public GenericObjectPool<StatefulConnection<String, String>> test(){
    RedisURI node1 = RedisURI.create("localhost", 7001);
    RedisURI node2 = RedisURI.create("localhost", 7002);
    RedisURI node3 = RedisURI.create("localhost", 7003);

    RedisClusterClient client = RedisClusterClient.create(Arrays.asList(node1, node2, node3));
    //StatefulConnection<String, String> conn = client.connect();

    /*
    RedisURI uri = RedisURI.create("localhost", 6379);
    RedisClient client = RedisClient.create(uri);
    */

    GenericObjectPool<StatefulConnection<String, String>> pool =
        ConnectionPoolSupport.createGenericObjectPool(() -> client.connect(), new GenericObjectPoolConfig(), true);
    return pool;
  }

  @Bean
  public LettuceTemplate lettuceTemplate(GenericObjectPool<StatefulConnection<String, String>> pool){
    return new LettuceTemplate(pool);
  }

  @Bean
  public SummaryDao summaryDao(LettuceTemplate lettuceTemplate){
    return new SummaryDao(lettuceTemplate);
  }

  @Bean
  public SliceDao sliceDao(LettuceTemplate lettuceTemplate){
    return new SliceDao(lettuceTemplate);
  }

  @Bean
  public MyService myService(LettuceTemplate lettuceTemplate, SummaryDao summaryDao, SliceDao sliceDao){
    return new MyService(lettuceTemplate, summaryDao, sliceDao);
  }
}
