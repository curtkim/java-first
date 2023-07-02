--redis.call('multi')
redis.call('set', KEYS[1], ARGV[1])
redis.call('rpush', KEYS[2], unpack(ARGV, 2))
--redis.call('exec')
return true;
