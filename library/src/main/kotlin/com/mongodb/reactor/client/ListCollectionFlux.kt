package com.mongodb.reactor.client

import com.mongodb.reactivestreams.client.ListCollectionsPublisher
import org.bson.conversions.Bson
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.TimeUnit

/**
 * Wraps MongoDB's reactivestreams driver [ListCollectionsPublisher] class into a Reactor's [Flux] child class.
 * Delegates every call to the original MongoDB's reactivestreams driver class but transform ReactiveStream's Publisher return types
 * into either [Flux] or [Mono].
 * @param T the type of the result
 * @param delegate original MongoDB's reactivestreams driver class to which all the calls are delegated
 * @see Flux
 * @see ListCollectionsPublisher
 */
public open class ListCollectionFlux<T>(private val delegate: ListCollectionsPublisher<T>) : Flux<T>(), ListCollectionsPublisher<T> {

    override fun filter(filter: Bson?): ListCollectionFlux<T> = delegate.filter(filter).toReactor()

    override fun maxTime(maxTime: Long, timeUnit: TimeUnit): ListCollectionFlux<T> = delegate.maxTime(maxTime, timeUnit).toReactor()

    override fun batchSize(batchSize: Int): ListCollectionFlux<T> = delegate.batchSize(batchSize).toReactor()

    override fun first(): Mono<T> = delegate.first().toMono()

    override fun subscribe(actual: CoreSubscriber<in T>) {
        delegate.subscribe(actual)
    }
}
