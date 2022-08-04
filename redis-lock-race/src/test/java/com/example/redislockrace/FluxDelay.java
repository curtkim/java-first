package com.example.redislockrace;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class FluxDelay {
    public static void main(String[] args){
        Flux.range(1, 100)
                .delayElements(Duration.ofMillis(100))
                .flatMap((it)-> Flux.range(1, it))
                .doOnNext((it)-> {
                    System.out.println(Thread.currentThread().getName() + " " +it);
                })
                .collectList().block();
    }
}
