package com.mongodb.reactor.client

import com.mongodb.reactivestreams.client.ListDatabasesPublisher
import org.bson.conversions.Bson
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.TimeUnit

public class ListDatabaseFlux<T>(private val delegate: ListDatabasesPublisher<T>) : Flux<T>(), ListDatabasesPublisher<T> {

    override fun filter(filter: Bson?): ListDatabaseFlux<T> = delegate.filter(filter).toReactor()

    override fun maxTime(maxTime: Long, timeUnit: TimeUnit): ListDatabaseFlux<T> = delegate.maxTime(10, TimeUnit.SECONDS).toReactor()

    override fun nameOnly(nameOnly: Boolean?): ListDatabaseFlux<T> = delegate.nameOnly(nameOnly).toReactor()

    override fun authorizedDatabasesOnly(authorizedDatabasesOnly: Boolean?): ListDatabaseFlux<T> {
        return delegate.authorizedDatabasesOnly(authorizedDatabasesOnly).toReactor()
    }

    override fun batchSize(batchSize: Int): ListDatabaseFlux<T> = delegate.batchSize(batchSize).toReactor()

    override fun first(): Mono<T> = delegate.first().toMono()

    override fun subscribe(actual: CoreSubscriber<in T>) {
        delegate.subscribe(actual)
    }
}
