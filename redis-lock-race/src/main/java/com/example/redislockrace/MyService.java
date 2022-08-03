package com.example.redislockrace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class MyService {
  private static final Logger logger = LoggerFactory.getLogger(MyService.class);

  @Autowired
  private StringRedisTemplate redisTemplate;


  static String listKey(String id){
    return String.format("list:%s", id);
  }
  static String sumKey(String id){
    return String.format("sum:%s", id);
  }
//  static String verKey(String id){
//    return String.format("ver:%s", id);
//  }
  static String statusKey(String id){
    return String.format("status:%s", id);
  }

  static long generateRandom(long max){
    return (long)(new Random().nextDouble()*max);
  }

  // 0~100ms
  static void sleepUnchecked(long millSecond){
    try {
      Thread.sleep(millSecond);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  List<Object> doit(String id, String value, boolean isEnd){
    List<Object> results = redisTemplate.execute(new SessionCallback<List<Object>>() {
      @Override
      public List<Object> execute(RedisOperations operations) throws DataAccessException {
        // 1.watch
        if(isEnd) {
          //redisTemplate.opsForValue().increment(verKey(id));
          operations.opsForValue().set(statusKey(id), Status.ENDING.name());
        }
        else {
          Status oldStatus = Status.valueOf(operations.opsForValue().get(statusKey(id)).toString());
          if(oldStatus != Status.READY)
            return Arrays.asList();

          operations.opsForValue().set(statusKey(id), Status.APPENDING.name());
          //redisTemplate.watch(verKey(id));
        }

        // 2.get
        String strSum = redisTemplate.opsForValue().get(sumKey(id));
        if(strSum == null)
          return Arrays.asList();

        // 3.calculate
        int sum = Integer.parseInt(strSum);
        int newSum = sum + Integer.parseInt(value);
        sleepUnchecked(generateRandom(100));

        // 4.save
        operations.multi();
        if(isEnd){
          operations.opsForValue().set(statusKey(id), Status.COMPLETE.name());
        }
        else {
          //operations.opsForValue().increment(verKey(id));
          operations.opsForValue().set(statusKey(id), Status.READY.name());
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
        operations.opsForValue().set(statusKey(id), Status.READY.name());
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

  public String get(String id) {
    List<String> strValues = redisTemplate.opsForList().range(listKey(id), 0, -1);
    List<Integer> values = strValues.stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());

    String sum = redisTemplate.opsForValue().get(sumKey(id));

    int valueSum = values.stream().mapToInt(s-> s).sum();
    boolean isValid = Integer.parseInt(sum) == valueSum;
    boolean isSorted = Utils.isSorted(values);

    return String.format("%s %s\n%s = %s (%s)",
        isValid, isSorted,
        sum, valueSum, strValues.stream().collect(Collectors.joining("+")));
  }

  public Boolean isValid(String id){
    List<String> values = redisTemplate.opsForList().range(listKey(id), 0, -1);
    String strSum = redisTemplate.opsForValue().get(sumKey(id));
    int sum = values.stream().mapToInt(s-> Integer.parseInt(s)).sum();
    return sum == Integer.parseInt(strSum);
  }
}
