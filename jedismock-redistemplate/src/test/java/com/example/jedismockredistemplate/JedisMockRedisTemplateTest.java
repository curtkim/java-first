package com.example.jedismockredistemplate;

import com.github.fppt.jedismock.RedisServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JedisMockRedisTemplateTest {

    private RedisServer server;
    private StringRedisTemplate redisTemplate;


    @BeforeEach
    public void before() throws IOException {
        server = RedisServer
                .newRedisServer()
                .start();

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(server.getHost(), server.getBindPort());

        JedisConnectionFactory fac = new JedisConnectionFactory(config);
        fac.afterPropertiesSet();
        redisTemplate = new StringRedisTemplate(fac);
    }

    @AfterEach
    public void after() throws IOException {
        server.stop();
    }

    @Test
    public void testSetGet() {
        final String key = "key";
        final String value = "abc";
        redisTemplate.opsForValue().set(key, value);
        assertEquals(value, redisTemplate.opsForValue().get(key));
    }
}
