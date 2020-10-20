package com.mongodb.reactor.client

import com.mongodb.reactivestreams.client.ListIndexesPublisher
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.TimeUnit

public class ListIndexesFlux<T>(private val delegate: ListIndexesPublisher<T>) : Flux<T>(), ListIndexesPublisher<T> {

    override fun maxTime(maxTime: Long, timeUnit: TimeUnit): ListIndexesFlux<T> = delegate.maxTime(maxTime, timeUnit).toReactor()

    override fun batchSize(batchSize: Int): ListIndexesFlux<T> = delegate.batchSize(batchSize).toReactor()

    override fun first(): Mono<T> = delegate.first().toMono()

    override fun subscribe(actual: CoreSubscriber<in T>) {
        delegate.subscribe(actual)
    }
}
