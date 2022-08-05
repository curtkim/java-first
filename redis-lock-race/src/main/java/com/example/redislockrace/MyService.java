package com.example.redislockrace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class MyService {

    @Autowired
    protected StringRedisTemplate redisTemplate;

    abstract void begin(String id, String value);
    abstract void append(String id, String value);

    abstract void end(String id, String value);



    static String listKey(String id){
        return String.format("list:%s", id);
    }
    static String sumKey(String id){
        return String.format("sum:%s", id);
    }
    static String verKey(String id){
      return String.format("ver:%s", id);
    }
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
    public String get(String id) {
        List<String> strValues = redisTemplate.opsForList().range(listKey(id), 0, -1);
        List<Integer> values = strValues.stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());

        //Status status = Status.valueOf(redisTemplate.opsForValue().get(statusKey(id)));
        String sum = redisTemplate.opsForValue().get(sumKey(id));


        int valueSum = values.stream().mapToInt(s-> s).sum();
        boolean isValid = Integer.parseInt(sum) == valueSum;
        boolean isSorted = Utils.isSorted(values);

        return String.format("id=%s : isValid=%s isSorted=%s\n%s = %s (%s)",
                id, isValid, isSorted,
                sum, valueSum, strValues.stream().collect(Collectors.joining("+")));
    }

    public Boolean isValid(String id){
        List<String> values = redisTemplate.opsForList().range(listKey(id), 0, -1);
        String strSum = redisTemplate.opsForValue().get(sumKey(id));
        int sum = values.stream().mapToInt(s-> Integer.parseInt(s)).sum();
        return sum == Integer.parseInt(strSum);
    }
}
