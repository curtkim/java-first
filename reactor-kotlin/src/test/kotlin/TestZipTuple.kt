import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import reactor.util.function.Tuple2

class TestZipTuple {

    operator fun <T1, T2> Tuple2<T1, T2>.component1(): T1 {
        return this.t1;
    }

    operator fun <T1, T2> Tuple2<T1, T2>.component2(): T2 {
        return this.t2;
    }

    @Test
    fun test() {
        StepVerifier.create(
            Mono.just("a").zipWith(Mono.just("b"))
                .map { (a, b) -> a + b }
        )
            .expectNext("ab")
            .verifyComplete();
    }
}