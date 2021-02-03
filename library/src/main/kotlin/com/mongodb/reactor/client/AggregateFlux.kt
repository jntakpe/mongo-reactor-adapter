package com.mongodb.reactor.client

import com.mongodb.ExplainVerbosity
import com.mongodb.client.model.Collation
import com.mongodb.reactivestreams.client.AggregatePublisher
import org.bson.Document
import org.bson.conversions.Bson
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.TimeUnit

/**
 * Wraps MongoDB's reactivestreams driver [AggregatePublisher] class into a Reactor's [Flux] child class.
 * Delegates every call to the original MongoDB's reactivestreams driver class but transform ReactiveStream's Publisher return types
 * into either [Flux] or [Mono].
 * @param T the type of the result
 * @param delegate original MongoDB's reactivestreams driver class to which all the calls are delegated
 * @see Flux
 * @see AggregatePublisher
 */
public open class AggregateFlux<T>(private val delegate: AggregatePublisher<T>) : Flux<T>(), AggregatePublisher<T> {

    override fun allowDiskUse(allowDiskUse: Boolean?): AggregateFlux<T> = delegate.allowDiskUse(allowDiskUse).toReactor()

    override fun maxTime(maxTime: Long, timeUnit: TimeUnit): AggregateFlux<T> = delegate.maxTime(maxTime, timeUnit).toReactor()

    override fun maxAwaitTime(maxAwaitTime: Long, timeUnit: TimeUnit): AggregateFlux<T> {
        return delegate.maxAwaitTime(maxAwaitTime, timeUnit).toReactor()
    }

    override fun bypassDocumentValidation(bypassDocumentValidation: Boolean?): AggregateFlux<T> {
        return delegate.bypassDocumentValidation(bypassDocumentValidation).toReactor()
    }

    override fun toCollection(): Mono<Void> = delegate.toCollection().toMono()

    override fun collation(collation: Collation?): AggregateFlux<T> = delegate.collation(collation).toReactor()

    override fun comment(comment: String?): AggregateFlux<T> = delegate.comment(comment).toReactor()

    override fun hint(hint: Bson?): AggregateFlux<T> = delegate.hint(hint).toReactor()

    override fun batchSize(batchSize: Int): AggregateFlux<T> = delegate.batchSize(batchSize).toReactor()

    override fun first(): Mono<T> = delegate.first().toMono()

    override fun explain(): Mono<Document> = delegate.explain().toMono()

    override fun explain(verbosity: ExplainVerbosity): Mono<Document> = delegate.explain(verbosity).toMono()

    override fun <E : Any> explain(explainResultClass: Class<E>): Mono<E> = delegate.explain(explainResultClass).toMono()

    override fun <E : Any> explain(explainResultClass: Class<E>, verbosity: ExplainVerbosity?): Mono<E> {
        return delegate.explain(explainResultClass, verbosity).toMono()
    }

    override fun subscribe(actual: CoreSubscriber<in T>) {
        delegate.subscribe(actual)
    }
}
