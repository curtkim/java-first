package com.example.suspendjobchannel

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.coroutines.coroutineContext

@RestController
class MyController(val channel : Channel<Int>) {

    @GetMapping("/signal")
    suspend fun signal(value: Int): Int {
        // [Context1{reactor.onDiscard.local=reactor.core.publisher.Operators$$Lambda$660/1022905142@5e00ac0a}, MonoCoroutine{Active}@7491f60e, Dispatchers.Unconfined]
        println(coroutineContext.toString())
        println("${Thread.currentThread()}: signal rest")
        channel.send(value)
        return value
    }
}