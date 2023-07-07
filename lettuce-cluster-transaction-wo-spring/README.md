## 개요
- Redis Cluster에서 watch/multi/exec를 지원하는 RedisTemplate을 만들자
- clusterConnection에서 key를 보고 node를 찾은다음에 해당 node의 connection을 redisTemplate에 태운다.
