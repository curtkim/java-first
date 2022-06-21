import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class FluxLogMain {
  private static final Logger logger = LoggerFactory.getLogger(FluxLogMain.class);

  public static void main(String[] args){
    Flux.just(1, 2, 4, 5, 6)
        .log()
        .map(x -> x * 2)
        .subscribe(x -> logger.info("next: {}", x));
  }
}
