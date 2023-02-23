package redisson;

import org.redisson.api.RMapReactive;
import org.redisson.api.RTransactionReactive;
import org.redisson.spring.transaction.ReactiveRedissonTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

public class TransactionDao {
  @Autowired
  private ReactiveRedissonTransactionManager transactionManager;

  @Transactional
  public Mono<Void> commitData() {
    Mono<RTransactionReactive> transaction = transactionManager.getCurrentTransaction();
    return transaction.flatMap(t -> {
      RMapReactive<String, String> map = t.getMap("test1");
      return map.put("1", "2");
    }).then();
  }
}
