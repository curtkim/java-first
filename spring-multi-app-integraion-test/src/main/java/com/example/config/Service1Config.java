package com.example.config;

import com.example.big.BigObject;
import com.example.dao.ADao;
import com.example.dao.BDao;
import com.example.service.Service1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Service1Config {
  @Bean
  public Service1 service1(
      ADao aDao,
      BDao bDao,
      BigObject bigObject
  ) {
    return new Service1(aDao, bDao, bigObject);
  }

}
