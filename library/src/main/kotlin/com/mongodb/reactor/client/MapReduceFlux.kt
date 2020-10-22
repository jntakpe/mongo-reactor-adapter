package com.mongodb.reactor.client

import com.mongodb.client.model.Collation
import com.mongodb.client.model.MapReduceAction
import com.mongodb.reactivestreams.client.MapReducePublisher
import org.bson.conversions.Bson
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.TimeUnit

/**
 * Wraps MongoDB's reactivestreams driver [MapReducePublisher] class into a Reactor's [Flux] child class.
 * Delegates every call to the original MongoDB's reactivestreams driver class but transform ReactiveStream's Publisher return types
 * into either [Flux] or [Mono].
 * @param T the type of the result
 * @param delegate original MongoDB's reactivestreams driver class to which all the calls are delegated
 * @see Flux
 * @see MapReducePublisher
 */
public class MapReduceFlux<T>(private val delegate: MapReducePublisher<T>) : Flux<T>(), MapReducePublisher<T> {

    override fun collectionName(collectionName: String): MapReduceFlux<T> = delegate.collectionName(collectionName).toReactor()

    override fun finalizeFunction(finalizeFunction: String?): MapReduceFlux<T> = delegate.finalizeFunction(finalizeFunction).toReactor()

    override fun scope(scope: Bson?): MapReduceFlux<T> = delegate.scope(scope).toReactor()

    override fun sort(sort: Bson?): MapReduceFlux<T> = delegate.sort(sort).toReactor()

    override fun filter(filter: Bson?): MapReduceFlux<T> = delegate.filter(filter).toReactor()

    override fun limit(limit: Int): MapReduceFlux<T> = delegate.limit(limit).toReactor()

    override fun jsMode(jsMode: Boolean): MapReduceFlux<T> = delegate.jsMode(jsMode).toReactor()

    override fun verbose(verbose: Boolean): MapReduceFlux<T> = delegate.verbose(verbose).toReactor()

    override fun maxTime(maxTime: Long, timeUnit: TimeUnit): MapReduceFlux<T> = delegate.maxTime(maxTime, timeUnit).toReactor()

    override fun action(action: MapReduceAction): MapReduceFlux<T> = delegate.action(action).toReactor()

    override fun databaseName(databaseName: String?): MapReduceFlux<T> = delegate.databaseName(databaseName).toReactor()

    override fun sharded(sharded: Boolean): MapReduceFlux<T> = delegate.sharded(sharded).toReactor()

    override fun nonAtomic(nonAtomic: Boolean): MapReduceFlux<T> = delegate.nonAtomic(nonAtomic).toReactor()

    override fun bypassDocumentValidation(bypassDocumentValidation: Boolean?): MapReduceFlux<T> {
        return delegate.bypassDocumentValidation(bypassDocumentValidation).toReactor()
    }

    override fun toCollection(): Mono<Void> = delegate.toCollection().toMono()

    override fun collation(collation: Collation?): MapReduceFlux<T> = delegate.collation(collation).toReactor()

    override fun batchSize(batchSize: Int): MapReduceFlux<T> = delegate.batchSize(batchSize).toReactor()

    override fun first(): Mono<T> = delegate.first().toMono()

    override fun subscribe(actual: CoreSubscriber<in T>) {
        delegate.subscribe(actual)
    }
}
