import org.junit.Test;
import reactor.core.publisher.Flux;

public class FluxTest {

  @Test
  public void test() {
    Flux.just("1", "2")
        .concatMap(body -> {
          //return Mono.delay(Duration.ofSeconds(1)).map(it -> body);
          return Flux.just(body, body);
        })
        .subscribe(System.out::println);

  }
}
