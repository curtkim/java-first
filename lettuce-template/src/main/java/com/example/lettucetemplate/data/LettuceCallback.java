package com.example.lettucetemplate.data;

import io.lettuce.core.api.sync.*;

public interface LettuceCallback<K, V, T> {
  T execute(RedisStringCommands<K, V> stringCommands,
                RedisListCommands<K, V> listCommands,
                RedisKeyCommands<K, V> keyCommands);

}
