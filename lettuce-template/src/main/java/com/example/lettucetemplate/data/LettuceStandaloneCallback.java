package com.example.lettucetemplate.data;

import io.lettuce.core.api.sync.RedisKeyCommands;
import io.lettuce.core.api.sync.RedisListCommands;
import io.lettuce.core.api.sync.RedisStringCommands;
import io.lettuce.core.api.sync.RedisTransactionalCommands;

public interface LettuceStandaloneCallback<K, V, T> {
  T execute(RedisStringCommands<K, V> stringCommands,
                RedisListCommands<K, V> listCommands,
                RedisKeyCommands<K, V> keyCommands,
                RedisTransactionalCommands<K, V> txCommands);

}
