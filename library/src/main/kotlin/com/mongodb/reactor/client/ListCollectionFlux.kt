package com.mongodb.reactor.client

import com.mongodb.reactivestreams.client.ListCollectionsPublisher
import org.bson.conversions.Bson
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.TimeUnit

public class ListCollectionFlux<T>(private val delegate: ListCollectionsPublisher<T>) : Flux<T>(), ListCollectionsPublisher<T> {

    override fun filter(filter: Bson?): ListCollectionFlux<T> = delegate.filter(filter).toReactor()

    override fun maxTime(maxTime: Long, timeUnit: TimeUnit): ListCollectionFlux<T> = delegate.maxTime(maxTime, timeUnit).toReactor()

    override fun batchSize(batchSize: Int): ListCollectionFlux<T> = delegate.batchSize(batchSize).toReactor()

    override fun first(): Mono<T> = delegate.first().toMono()

    override fun subscribe(actual: CoreSubscriber<in T>) {
        delegate.subscribe(actual)
    }
}