package com.example.config;

import com.example.big.BigObject;
import com.example.dao.ADao;
import com.example.dao.BDao;
import com.example.service.Service2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Service2Config {
  @Bean
  public Service2 service2(
      ADao aDao,
      BDao bDao
  ) {
    return new Service2(aDao, bDao);
  }

}
