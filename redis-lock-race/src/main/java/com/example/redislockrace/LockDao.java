package com.example.redislockrace;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Date;

public class LockDao {
    private StringRedisTemplate redisTemplate;
    private String prefix;

    public LockDao(StringRedisTemplate redisTemplate, String prefix) {
        this.redisTemplate = redisTemplate;
        this.prefix = prefix;
    }

    String key(String id) {
        return prefix+ ":" + id;
    }
    public boolean lock(String id) {
        String time = new Date().getTime() + "";
        return redisTemplate.opsForValue().setIfAbsent(key(id), time);
    }

    public void release(String id) {
        redisTemplate.delete(key(id));
    }

}
