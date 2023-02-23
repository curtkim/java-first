package tx;

import example.EmbededRedisConfig;
import io.lettuce.core.TransactionResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;


@Import(EmbededRedisConfig.class)
@DataRedisTest
public class MyServiceTest {

  @Autowired
  private MyService myService;

  @Test
  public void addAndCount(){
    TransactionResult result = myService.addAndCount("oven", "2").block();

    long result1 = result.get(0);
    long result2 = result.get(1);

    System.out.println(result1 +" " + result2);
  }
}
