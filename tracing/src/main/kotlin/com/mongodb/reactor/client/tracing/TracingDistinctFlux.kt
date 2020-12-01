package com.mongodb.reactor.client.tracing

import brave.Tracing
import com.mongodb.reactivestreams.client.DistinctPublisher
import com.mongodb.reactor.client.DistinctFlux
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux

/**
 * Wraps MongoDB's reactivestreams driver [DistinctPublisher] class into a Reactor's [Flux] delegating most of the heavy lifting to
 * [TracingDistinctFlux]. Subscribes using a subscriber that continues OpenTracing's spans.
 * @param T the type of the result
 * @param delegate original MongoDB's reactivestreams driver class to which all the calls are delegated
 * @see TracingDistinctFlux
 * @see Flux
 * @see DistinctPublisher
 */
public class TracingDistinctFlux<T>(private val delegate: DistinctPublisher<T>, private val tracing: Tracing) :
    DistinctFlux<T>(delegate) {

    override fun subscribe(actual: CoreSubscriber<in T>) {
        delegate.subscribe(actual.toTracing(tracing))
    }
}
