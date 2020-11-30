package com.mongodb.reactor.client

import com.mongodb.client.model.Collation
import com.mongodb.reactivestreams.client.DistinctPublisher
import org.bson.conversions.Bson
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.TimeUnit

/**
 * Wraps MongoDB's reactivestreams driver [DistinctPublisher] class into a Reactor's [Flux] child class.
 * Delegates every call to the original MongoDB's reactivestreams driver class but transform ReactiveStream's Publisher return types
 * into either [Flux] or [Mono].
 * @param T the type of the result
 * @param delegate original MongoDB's reactivestreams driver class to which all the calls are delegated
 * @see Flux
 * @see DistinctPublisher
 */
public open class DistinctFlux<T>(private val delegate: DistinctPublisher<T>) : Flux<T>(), DistinctPublisher<T> {

    override fun filter(filter: Bson?): DistinctFlux<T> = delegate.filter(filter).toReactor()

    override fun maxTime(maxTime: Long, timeUnit: TimeUnit): DistinctFlux<T> = delegate.maxTime(maxTime, timeUnit).toReactor()

    override fun collation(collation: Collation?): DistinctFlux<T> = delegate.collation(collation).toReactor()

    override fun batchSize(batchSize: Int): DistinctFlux<T> = delegate.batchSize(batchSize).toReactor()

    override fun first(): Mono<T> = delegate.first().toMono()

    override fun subscribe(actual: CoreSubscriber<in T>) {
        delegate.subscribe(actual)
    }
}
