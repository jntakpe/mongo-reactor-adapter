package com.mongodb.reactor.client

import com.mongodb.CursorType
import com.mongodb.ExplainVerbosity
import com.mongodb.client.model.Collation
import com.mongodb.reactivestreams.client.FindPublisher
import org.bson.Document
import org.bson.conversions.Bson
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.TimeUnit

/**
 * Wraps MongoDB's reactivestreams driver [FindFlux] class into a Reactor's [Flux] child class.
 * Delegates every call to the original MongoDB's reactivestreams driver class but transform ReactiveStream's Publisher return types
 * into either [Flux] or [Mono].
 * @param T the type of the result
 * @param delegate original MongoDB's reactivestreams driver class to which all the calls are delegated
 * @see Flux
 * @see FindFlux
 */
public open class FindFlux<T>(private val delegate: FindPublisher<T>) : Flux<T>(), FindPublisher<T> {

    override fun first(): Mono<T> = delegate.first().toMono()

    override fun filter(filter: Bson?): FindFlux<T> = delegate.filter(filter).toReactor()

    override fun limit(limit: Int): FindFlux<T> = delegate.limit(limit).toReactor()

    override fun skip(skip: Int): FindFlux<T> = delegate.skip(skip).toReactor()

    override fun maxTime(maxTime: Long, timeUnit: TimeUnit): FindFlux<T> = delegate.maxTime(maxTime, timeUnit).toReactor()

    override fun maxAwaitTime(maxAwaitTime: Long, timeUnit: TimeUnit): FindFlux<T> =
        delegate.maxAwaitTime(maxAwaitTime, timeUnit).toReactor()

    override fun projection(projection: Bson?): FindFlux<T> = delegate.projection(projection).toReactor()

    override fun sort(sort: Bson?): FindFlux<T> = delegate.sort(sort).toReactor()

    override fun noCursorTimeout(noCursorTimeout: Boolean): FindFlux<T> = delegate.noCursorTimeout(noCursorTimeout).toReactor()

    override fun oplogReplay(oplogReplay: Boolean): FindFlux<T> = delegate.oplogReplay(oplogReplay).toReactor()

    override fun partial(partial: Boolean): FindFlux<T> = delegate.partial(partial).toReactor()

    override fun cursorType(cursorType: CursorType): FindFlux<T> = delegate.cursorType(cursorType).toReactor()

    override fun collation(collation: Collation?): FindFlux<T> = delegate.collation(collation).toReactor()

    override fun comment(comment: String?): FindFlux<T> = delegate.comment(comment).toReactor()

    override fun hint(hint: Bson?): FindFlux<T> = delegate.hint(hint).toReactor()

    override fun hintString(hint: String?): FindFlux<T> = delegate.hintString(hint).toReactor()

    override fun max(max: Bson?): FindFlux<T> = delegate.max(max).toReactor()

    override fun min(min: Bson?): FindFlux<T> = delegate.min(min).toReactor()

    override fun returnKey(returnKey: Boolean): FindFlux<T> = delegate.returnKey(returnKey).toReactor()

    override fun showRecordId(showRecordId: Boolean): FindFlux<T> = delegate.showRecordId(showRecordId).toReactor()

    override fun batchSize(batchSize: Int): FindFlux<T> = delegate.batchSize(batchSize).toReactor()

    override fun allowDiskUse(allowDiskUse: Boolean?): FindFlux<T> = delegate.allowDiskUse(allowDiskUse).toReactor()

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
