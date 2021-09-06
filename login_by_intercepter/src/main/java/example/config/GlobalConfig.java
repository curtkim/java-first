package example.config;

import example.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalConfig {

  @Bean
  public UserService userService(){
    return new UserService();
  }
}
