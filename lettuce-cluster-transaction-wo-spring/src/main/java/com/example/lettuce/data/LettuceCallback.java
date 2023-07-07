package com.example.lettuce.data;

import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public interface LettuceCallback<T> {
  T execute(RedisCommands<String, String> commands);
}
