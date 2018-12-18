package tutorial.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

public class MapDao {

  @Autowired
  private StringRedisTemplate redisTemplate;

  public void put(String key, String value){
    redisTemplate.opsForValue().set(key, value);
  }

  public String get(String key){
    return redisTemplate.opsForValue().get(key);
  }

}
