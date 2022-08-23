package com.example.redislockrace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;


@Service
public class MyService3 extends MyService{

  private static final Logger logger = LoggerFactory.getLogger(MyService3.class);

  static String endKey(String id){
    return String.format("end:%s", id);
  }

  @Override
  void begin(String id, String value) {
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

  @Override
  void append(String id, String value) {
    List<Object> results = redisTemplate.execute(new SessionCallback<List<Object>>() {
      @Override
      public List<Object> execute(RedisOperations operations) throws DataAccessException {
        operations.watch(sumKey(id));

        // end후에 호출되는 경우에
        if(redisTemplate.hasKey(endKey(id)))
          return Arrays.asList();

        // begin전에 호출되는 경우에
        String strSum = (String)operations.opsForValue().get(sumKey(id));
        if(strSum == null)
          return Arrays.asList();

        // 3.calculate
        int newSum = Integer.parseInt(strSum)+ Integer.parseInt(value);
        sleepUnchecked(generateRandom(100));

        // save
        operations.multi();
        operations.opsForList().rightPush(listKey(id), value);
        operations.opsForValue().set(sumKey(id), newSum+"");
        return operations.exec();
      }
    });
    logger.info("{} {} {}", id, value, results);
  }

  @Override
  void end(String id, String value) {
    List<Object> results = redisTemplate.execute(new SessionCallback<List<Object>>() {
      @Override
      public List<Object> execute(RedisOperations operations) throws DataAccessException {
        // 1.guard
        operations.expire(sumKey(id), Duration.ofMinutes(5));
        operations.opsForValue().set(endKey(id), "O");

        // 2.get
        String strSum = (String)operations.opsForValue().get(sumKey(id));
        if(strSum == null)
          return Arrays.asList();

        // 3.calculate
        int newSum = Integer.parseInt(strSum)+ Integer.parseInt(value);
        sleepUnchecked(generateRandom(100));

        // 4.save
        operations.multi();
        operations.opsForList().rightPush(listKey(id), value);
        operations.opsForValue().set(sumKey(id), newSum+"");
        return operations.exec();
      }
    });
    // 성공시 : [list_length, set_success?]
    // 실패시 : []

    logger.info("){} {} {}", id, value, results);
  }
}
