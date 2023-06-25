package com.example.config;

import com.example.big.BigObject;
import com.example.dao.ADao;
import com.example.dao.BDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class BigConfig {

  @Bean
  public BigObject bigObject(){
    log.info("bigObject created");
    return new BigObject();
  }
}
