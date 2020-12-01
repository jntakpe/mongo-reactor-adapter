package com.mongodb.reactor.client.tracing

import brave.Tracing
import com.mongodb.reactivestreams.client.ListIndexesPublisher
import com.mongodb.reactor.client.ListIndexesFlux
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux

/**
 * Wraps MongoDB's reactivestreams driver [ListIndexesPublisher] class into a Reactor's [Flux] delegating most of the heavy lifting to
 * [ListIndexesFlux]. Subscribes using a subscriber that continues OpenTracing's spans.
 * @param T the type of the result
 * @param delegate original MongoDB's reactivestreams driver class to which all the calls are delegated
 * @see ListIndexesFlux
 * @see Flux
 * @see ListIndexesPublisher
 */
public class TracingListIndexesFlux<T>(private val delegate: ListIndexesPublisher<T>, private val tracing: Tracing) :
    ListIndexesFlux<T>(delegate) {

    override fun subscribe(actual: CoreSubscriber<in T>) {
        delegate.subscribe(actual.toTracing(tracing))
    }
}
