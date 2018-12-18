package tutorial;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.kafka.annotation.EnableKafka;
import tutorial.dao.MapDao;

@Configuration
@EnableKafka
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class AppConfiguration {

  @Bean
  public MapDao mapDao(){
    return new MapDao();
  }
}
