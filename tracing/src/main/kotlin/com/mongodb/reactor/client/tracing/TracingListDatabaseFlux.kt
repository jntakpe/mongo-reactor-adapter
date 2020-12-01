package com.mongodb.reactor.client.tracing

import brave.Tracing
import com.mongodb.reactivestreams.client.ListDatabasesPublisher
import com.mongodb.reactor.client.ListDatabaseFlux
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux

/**
 * Wraps MongoDB's reactivestreams driver [ListDatabasesPublisher] class into a Reactor's [Flux] delegating most of the heavy lifting to
 * [ListDatabaseFlux]. Subscribes using a subscriber that continues OpenTracing's spans.
 * @param T the type of the result
 * @param delegate original MongoDB's reactivestreams driver class to which all the calls are delegated
 * @see ListDatabaseFlux
 * @see Flux
 * @see ListDatabasesPublisher
 */
public class TracingListDatabaseFlux<T>(private val delegate: ListDatabasesPublisher<T>, private val tracing: Tracing) :
    ListDatabaseFlux<T>(delegate) {

    override fun subscribe(actual: CoreSubscriber<in T>) {
        delegate.subscribe(actual.toTracing(tracing))
    }
}
