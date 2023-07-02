package com.example.redisscript;

import org.springframework.beans.factory.annotation.Qualifier;
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
  @Qualifier("cas")
  public RedisScript<Boolean> casScript() {
    Resource scriptSource = new ClassPathResource("scripts/compare_swap.lua");
    return RedisScript.of(scriptSource, Boolean.class);
  }

  @Bean
  @Qualifier("unpack")
  public RedisScript<Boolean> unpackScript() {
    Resource scriptSource = new ClassPathResource("scripts/set_and_rpush.lua");
    return RedisScript.of(scriptSource, Boolean.class);
  }
}
