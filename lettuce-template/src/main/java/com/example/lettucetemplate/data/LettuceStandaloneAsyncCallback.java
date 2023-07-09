package com.example.lettucetemplate.data;

import io.lettuce.core.api.async.RedisKeyAsyncCommands;
import io.lettuce.core.api.async.RedisListAsyncCommands;
import io.lettuce.core.api.async.RedisStringAsyncCommands;
import io.lettuce.core.api.async.RedisTransactionalAsyncCommands;
import io.lettuce.core.api.sync.RedisKeyCommands;
import io.lettuce.core.api.sync.RedisListCommands;
import io.lettuce.core.api.sync.RedisStringCommands;
import io.lettuce.core.api.sync.RedisTransactionalCommands;

public interface LettuceStandaloneAsyncCallback<K, V> {
  <T> T execute(RedisStringAsyncCommands<K, V> stringCommands,
                RedisListAsyncCommands<K, V> listCommands,
                RedisKeyAsyncCommands<K, V> keyCommands,
                RedisTransactionalAsyncCommands<K, V> txCommands);

}
