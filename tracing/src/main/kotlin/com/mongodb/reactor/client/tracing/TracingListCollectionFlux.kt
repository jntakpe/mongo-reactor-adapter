package com.mongodb.reactor.client.tracing

import brave.Tracing
import com.mongodb.reactivestreams.client.ListCollectionsPublisher
import com.mongodb.reactor.client.ListCollectionFlux
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux

/**
 * Wraps MongoDB's reactivestreams driver [ListCollectionsPublisher] class into a Reactor's [Flux] delegating most of the heavy lifting to
 * [ListCollectionFlux]. Subscribes using a subscriber that continues OpenTracing's spans.
 * @param T the type of the result
 * @param delegate original MongoDB's reactivestreams driver class to which all the calls are delegated
 * @see ListCollectionFlux
 * @see Flux
 * @see ListCollectionsPublisher
 */
public class TracingListCollectionFlux<T>(private val delegate: ListCollectionsPublisher<T>, private val tracing: Tracing) :
    ListCollectionFlux<T>(delegate) {

    override fun subscribe(actual: CoreSubscriber<in T>) {
        delegate.subscribe(actual.toTracing(tracing))
    }
}
