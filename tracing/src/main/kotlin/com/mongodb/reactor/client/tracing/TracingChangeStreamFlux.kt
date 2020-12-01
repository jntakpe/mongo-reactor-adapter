package com.mongodb.reactor.client.tracing

import brave.Tracing
import com.mongodb.client.model.changestream.ChangeStreamDocument
import com.mongodb.reactivestreams.client.ChangeStreamPublisher
import com.mongodb.reactor.client.ChangeStreamFlux
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux

/**
 * Wraps MongoDB's reactivestreams driver [ChangeStreamPublisher] class into a Reactor's [Flux] delegating most of the heavy lifting to
 * [ChangeStreamFlux]. Subscribes using a subscriber that continues OpenTracing's spans.
 * @param T the type of the result
 * @param delegate original MongoDB's reactivestreams driver class to which all the calls are delegated
 * @see ChangeStreamFlux
 * @see Flux
 * @see ChangeStreamPublisher
 */
public class TracingChangeStreamFlux<T>(private val delegate: ChangeStreamPublisher<T>, private val tracing: Tracing) :
    ChangeStreamFlux<T>(delegate) {

    override fun subscribe(actual: CoreSubscriber<in ChangeStreamDocument<T>>) {
        delegate.subscribe(actual.toTracing(tracing))
    }
}
