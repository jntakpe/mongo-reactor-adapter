package com.mongodb.reactor.client.tracing

import brave.Tracing
import com.mongodb.reactivestreams.client.MapReducePublisher
import com.mongodb.reactor.client.MapReduceFlux
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux

/**
 * Wraps MongoDB's reactivestreams driver [MapReducePublisher] class into a Reactor's [Flux] delegating most of the heavy lifting to
 * [MapReduceFlux]. Subscribes using a subscriber that continues OpenTracing's spans.
 * @param T the type of the result
 * @param delegate original MongoDB's reactivestreams driver class to which all the calls are delegated
 * @see MapReduceFlux
 * @see Flux
 * @see MapReducePublisher
 */
public class TracingMapReduceFlux<T>(private val delegate: MapReducePublisher<T>, private val tracing: Tracing) :
    MapReduceFlux<T>(delegate) {

    override fun subscribe(actual: CoreSubscriber<in T>) {
        delegate.subscribe(actual.toTracing(tracing))
    }
}
