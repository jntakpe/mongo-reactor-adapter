package com.mongodb.reactor.client.tracing

import brave.Tracing
import com.mongodb.reactivestreams.client.AggregatePublisher
import com.mongodb.reactor.client.AggregateFlux
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux

/**
 * Wraps MongoDB's reactivestreams driver [AggregatePublisher] class into a Reactor's [Flux] delegating most of the heavy lifting to
 * [AggregateFlux]. Subscribes using a subscriber that continues OpenTracing's spans.
 * @param T the type of the result
 * @param delegate original MongoDB's reactivestreams driver class to which all the calls are delegated
 * @see AggregateFlux
 * @see Flux
 * @see AggregatePublisher
 */
public class TracingAggregateFlux<T>(private val delegate: AggregatePublisher<T>, private val tracing: Tracing) :
    AggregateFlux<T>(delegate) {

    override fun subscribe(actual: CoreSubscriber<in T>) {
        delegate.subscribe(actual.toTracing(tracing))
    }
}
