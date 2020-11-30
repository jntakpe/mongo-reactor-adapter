package com.mongodb.reactor.client

import com.mongodb.reactivestreams.client.ListIndexesPublisher
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.TimeUnit

/**
 * Wraps MongoDB's reactivestreams driver [ListIndexesPublisher] class into a Reactor's [Flux] child class.
 * Delegates every call to the original MongoDB's reactivestreams driver class but transform ReactiveStream's Publisher return types
 * into either [Flux] or [Mono].
 * @param T the type of the result
 * @param delegate original MongoDB's reactivestreams driver class to which all the calls are delegated
 * @see Flux
 * @see ListIndexesPublisher
 */
public open class ListIndexesFlux<T>(private val delegate: ListIndexesPublisher<T>) : Flux<T>(), ListIndexesPublisher<T> {

    override fun maxTime(maxTime: Long, timeUnit: TimeUnit): ListIndexesFlux<T> = delegate.maxTime(maxTime, timeUnit).toReactor()

    override fun batchSize(batchSize: Int): ListIndexesFlux<T> = delegate.batchSize(batchSize).toReactor()

    override fun first(): Mono<T> = delegate.first().toMono()

    override fun subscribe(actual: CoreSubscriber<in T>) {
        delegate.subscribe(actual)
    }
}
