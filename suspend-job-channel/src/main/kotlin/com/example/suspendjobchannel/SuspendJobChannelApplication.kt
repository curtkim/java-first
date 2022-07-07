package com.example.suspendjobchannel

import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import kotlin.concurrent.thread

@SpringBootApplication
class SuspendJobChannelApplication{

    @Bean
    fun channel() : Channel<Int> {
        return Channel<Int>()
    }

    @Bean
    fun jobThread(channel: Channel<Int>) : Thread {
        return thread() {
            runBlocking {
                launch {
                    for (y in channel) {
                        println(coroutineContext.toString())    // [StandaloneCoroutine{Active}@28a49d52, BlockingEventLoop@4bb14cac]
                        println("${Thread.currentThread()}: it's running.")
                        delay(1000)
                        println(y)
                    }
                }

                launch {
                    for (y in channel) {
                        println(coroutineContext.toString())    // [StandaloneCoroutine{Active}@3f461341, BlockingEventLoop@4bb14cac]
                        println("${Thread.currentThread()}: it's running.")
                        delay(1000)
                        println(y)
                    }
                }
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<SuspendJobChannelApplication>(*args)
}
