package com.example.config;

import com.example.dao.ADao;
import com.example.dao.BDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoConfig {

  @Bean
  public ADao aDao(){
    return new ADao();
  }

  @Bean
  public BDao bDao(){
    return new BDao();
  }
}
