package redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.spring.transaction.ReactiveRedissonTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class RedissonReactiveTransactionContextConfig {

  @Bean
  public ReactiveRedissonTransactionManager transactionManager(RedissonReactiveClient redisson) {
    return new ReactiveRedissonTransactionManager(redisson);
  }

  @Bean(destroyMethod = "shutdown")
  public RedissonReactiveClient redisson() {
    return Redisson.create().reactive();
  }
}

