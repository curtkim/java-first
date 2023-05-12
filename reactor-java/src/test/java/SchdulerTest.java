import java.time.Duration;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class SchdulerTest {

    public static long calculateLong(long val) {
        try {
            Thread.currentThread().sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return val;
    }

    @Test
    public void test() throws InterruptedException {
        long startTime = System.currentTimeMillis();

        Scheduler myPara = Schedulers.newParallel("myPara", 10);

        Flux.interval(Duration.ofMillis(100))
            .take(20)
            //.flatMap(v -> Flux.just(v).publishOn(myPara).map(SchdulerTest::calculateLong))
                .map(v -> Flux.just(v).publishOn(myPara).map(SchdulerTest::calculateLong))
                .flatMap(v -> v)
            .doOnNext(v -> System.out.println(String.format("%d %d %s", v, System.currentTimeMillis()- startTime, Thread.currentThread().getName())))
            .doOnComplete(()-> System.out.println("done"))
            .blockLast();
    }
}
