package com.mongodb.reactor.client

import com.mongodb.client.model.Collation
import com.mongodb.client.model.changestream.ChangeStreamDocument
import com.mongodb.client.model.changestream.FullDocument
import com.mongodb.reactivestreams.client.ChangeStreamPublisher
import org.bson.BsonDocument
import org.bson.BsonTimestamp
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.TimeUnit

/**
 * Wraps MongoDB's reactivestreams driver [ChangeStreamPublisher] class into a Reactor's [Flux] child class.
 * Delegates every call to the original MongoDB's reactivestreams driver class but transform ReactiveStream's Publisher return types
 * into either [Flux] or [Mono].
 * @param T the type of the result
 * @param delegate original MongoDB's reactivestreams driver class to which all the calls are delegated
 * @see Flux
 * @see ChangeStreamPublisher
 */
public open class ChangeStreamFlux<T>(private val delegate: ChangeStreamPublisher<T>) : Flux<ChangeStreamDocument<T>>(),
                                                                                        ChangeStreamPublisher<T> {

    override fun fullDocument(fullDocument: FullDocument): ChangeStreamFlux<T> = delegate.fullDocument(fullDocument).toReactor()

    override fun resumeAfter(resumeToken: BsonDocument): ChangeStreamFlux<T> = delegate.resumeAfter(resumeToken).toReactor()

    override fun startAtOperationTime(startAtOperationTime: BsonTimestamp): ChangeStreamFlux<T> {
        return delegate.startAtOperationTime(startAtOperationTime).toReactor()
    }

    override fun startAfter(startAfter: BsonDocument): ChangeStreamFlux<T> = delegate.startAfter(startAfter).toReactor()

    override fun maxAwaitTime(maxAwaitTime: Long, timeUnit: TimeUnit): ChangeStreamFlux<T> {
        return delegate.maxAwaitTime(maxAwaitTime, timeUnit).toReactor()
    }

    override fun collation(collation: Collation?): ChangeStreamFlux<T> = delegate.collation(collation).toReactor()

    override fun <TDocument : Any> withDocumentClass(clazz: Class<TDocument>): Flux<TDocument> {
        return from(delegate.withDocumentClass(clazz))
    }

    override fun batchSize(batchSize: Int): ChangeStreamFlux<T> = delegate.batchSize(batchSize).toReactor()

    override fun first(): Mono<ChangeStreamDocument<T>> = delegate.first().toMono()

    override fun subscribe(actual: CoreSubscriber<in ChangeStreamDocument<T>>) {
        return delegate.subscribe(actual)
    }
}
