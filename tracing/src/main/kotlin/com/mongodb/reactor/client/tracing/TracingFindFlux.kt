package com.mongodb.reactor.client.tracing

import brave.Tracing
import com.mongodb.reactivestreams.client.FindPublisher
import com.mongodb.reactor.client.FindFlux
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux

/**
 * Wraps MongoDB's reactivestreams driver [FindPublisher] class into a Reactor's [Flux] delegating most of the heavy lifting to
 * [TracingFindFlux]. Subscribes using a subscriber that continues OpenTracing's spans.
 * @param T the type of the result
 * @param delegate original MongoDB's reactivestreams driver class to which all the calls are delegated
 * @see TracingFindFlux
 * @see Flux
 * @see FindPublisher
 */
public class TracingFindFlux<T>(private val delegate: FindPublisher<T>, private val tracing: Tracing) :
    FindFlux<T>(delegate) {

    override fun subscribe(actual: CoreSubscriber<in T>) {
        delegate.subscribe(actual.toTracing(tracing))
    }
}
