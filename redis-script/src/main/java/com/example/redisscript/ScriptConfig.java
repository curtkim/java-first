package com.example.redisscript;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.script.RedisScript;

@Configuration
public class ScriptConfig {
  @Bean
  public RedisScript<String> echoScript() {
    Resource scriptSource = new ClassPathResource("scripts/echo.lua");
    return RedisScript.of(scriptSource, String.class);
  }

  @Bean
  public RedisScript<Boolean> casScript() {
    Resource scriptSource = new ClassPathResource("scripts/compare_swap.lua");
    return RedisScript.of(scriptSource, Boolean.class);
  }
}
