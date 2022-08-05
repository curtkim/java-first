package com.example.redislockrace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

//@Service
public class MyService1 extends MyService {
  private static final Logger logger = LoggerFactory.getLogger(MyService1.class);


  List<Object> doit(String id, String value, boolean isEnd){
    List<Object> results = redisTemplate.execute(new SessionCallback<List<Object>>() {
      @Override
      public List<Object> execute(RedisOperations operations) throws DataAccessException {
        // 1.watch
        if(isEnd) {
          redisTemplate.opsForValue().increment(verKey(id));
        }
        else {
          redisTemplate.watch(verKey(id));
        }

        // 2.get
        String strSum = redisTemplate.opsForValue().get(sumKey(id));
        if(strSum == null)
          return Arrays.asList();

        // 3.calculate
        int newSum = Integer.parseInt(strSum)+ Integer.parseInt(value);
        sleepUnchecked(generateRandom(100));

        // 4.save
        operations.multi();
        if(isEnd){
        }
        else {
          operations.opsForValue().increment(verKey(id));
        }

        operations.opsForList().rightPush(listKey(id), value);
        operations.opsForValue().set(sumKey(id), newSum+"");
        return operations.exec();
      }
    });
    // 성공시 : [list_length, set_success?]
    // 실패시 : []
    return results;
  }

  public void begin(String id, String value){
    List<Object> results = redisTemplate.execute(new SessionCallback<List<Object>>() {
      @Override
      public List<Object> execute(RedisOperations operations) throws DataAccessException {
        int newSum = Integer.parseInt(value);
        // save
        operations.multi();
        operations.opsForList().rightPush(listKey(id), value);
        operations.opsForValue().set(sumKey(id), newSum+"");
        return operations.exec();
      }
    });
    logger.info("{} {} {}", id, value, results);
  }

  public void append(String id, String value){
    List<Object> results = doit(id, value, false);
    logger.info("{} {} {}", id, value, results);
  }

  public void end(String id, String value){
    List<Object> results = doit(id, value, true);
    logger.info("{} {} {}", id, value, results);
  }

}
