package com.example.redislockrace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class FluxDelay {

  private static final Logger logger = LoggerFactory.getLogger(FluxDelay.class);

    public static void main(String[] args){
        /*
        Flux.range(1, 100)
                .delayElements(Duration.ofMillis(100))
                .flatMap((it)-> Flux.range(1, it))
                .doOnNext((it)-> {
                    System.out.println(Thread.currentThread().getName() + " " +it);
                })
                .collectList().block();
         */

        Flux.range(1, 30)
            .delayElements(Duration.ofMillis(50))
            .flatMap((it)-> {
              logger.info("start: {}", it);
              MyService.sleepUnchecked(100);
              return Mono.just(it);
            })
            .doOnNext((it)-> {
              logger.info("next: {}", it);
            })
            .collectList().block();
    }
}
