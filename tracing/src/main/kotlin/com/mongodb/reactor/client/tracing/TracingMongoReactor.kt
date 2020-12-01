package com.mongodb.reactor.client.tracing

import brave.Tracing
import brave.propagation.CurrentTraceContext
import brave.propagation.TraceContext
import com.mongodb.reactivestreams.client.AggregatePublisher
import com.mongodb.reactivestreams.client.ChangeStreamPublisher
import com.mongodb.reactivestreams.client.DistinctPublisher
import com.mongodb.reactivestreams.client.FindPublisher
import com.mongodb.reactivestreams.client.ListCollectionsPublisher
import com.mongodb.reactivestreams.client.ListDatabasesPublisher
import com.mongodb.reactivestreams.client.ListIndexesPublisher
import com.mongodb.reactivestreams.client.MapReducePublisher
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoCollection
import com.mongodb.reactivestreams.client.MongoDatabase
import com.mongodb.reactor.client.AggregateFlux
import com.mongodb.reactor.client.ChangeStreamFlux
import com.mongodb.reactor.client.DistinctFlux
import com.mongodb.reactor.client.FindFlux
import com.mongodb.reactor.client.ListCollectionFlux
import com.mongodb.reactor.client.ListDatabaseFlux
import com.mongodb.reactor.client.ListIndexesFlux
import com.mongodb.reactor.client.MapReduceFlux
import com.mongodb.reactor.client.ReactorMongoClient
import com.mongodb.reactor.client.ReactorMongoCollection
import com.mongodb.reactor.client.ReactorMongoDatabase
import org.reactivestreams.Publisher
import reactor.core.CoreSubscriber
import reactor.core.Fuseable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Operators
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import reactor.util.context.Context
import java.util.function.Function

/**
 * Extension that wraps MongoDB's [AggregatePublisher] of [T] into an [AggregateFlux] wrapper class
 * @receiver MongoDB's reactivestreams driver aggregate operation return class
 * @param T the type of the result
 * @param tracing OpenTracing's Brave instrumentation
 * @return Reactor wrapper for [AggregatePublisher]
 */
public fun <T> AggregatePublisher<T>.toTracingReactor(tracing: Tracing): TracingAggregateFlux<T> {
    return intoReactor(::TracingAggregateFlux, tracing)
}

/**
 * Extension that wraps MongoDB's [ChangeStreamPublisher] of [T] into an [ChangeStreamFlux] wrapper class
 * @receiver MongoDB's reactivestreams driver change stream operation return class
 * @param T the type of the result
 * @param tracing OpenTracing's Brave instrumentation
 * @return Reactor wrapper for [ChangeStreamPublisher]
 */
public fun <T> ChangeStreamPublisher<T>.toTracingReactor(tracing: Tracing): TracingChangeStreamFlux<T> {
    return intoReactor(::TracingChangeStreamFlux, tracing)
}

/**
 * Extension that wraps MongoDB's [DistinctPublisher] of [T] into an [DistinctFlux] wrapper class
 * @receiver MongoDB's reactivestreams driver distinct operation return class
 * @param T the type of the result
 * @param tracing OpenTracing's Brave instrumentation
 * @return Reactor wrapper for [DistinctPublisher]
 */
public fun <T> DistinctPublisher<T>.toTracingReactor(tracing: Tracing): TracingDistinctFlux<T> = intoReactor(::TracingDistinctFlux, tracing)

/**
 * Extension that wraps MongoDB's [FindPublisher] of [T] into an [FindFlux] wrapper class
 * @receiver MongoDB's reactivestreams driver find operation return class
 * @param T the type of the result
 * @param tracing OpenTracing's Brave instrumentation
 * @return Reactor wrapper for [FindPublisher]
 */
public fun <T> FindPublisher<T>.toTracingReactor(tracing: Tracing): TracingFindFlux<T> = intoReactor(::TracingFindFlux, tracing)

/**
 * Extension that wraps MongoDB's [ListCollectionsPublisher] of [T] into an [ListCollectionFlux] wrapper class
 * @receiver MongoDB's reactivestreams driver list collections operation return class
 * @param T the type of the result
 * @param tracing OpenTracing's Brave instrumentation
 * @return Reactor wrapper for [ListCollectionsPublisher]
 */
public fun <T> ListCollectionsPublisher<T>.toTracingReactor(tracing: Tracing): TracingListCollectionFlux<T> {
    return intoReactor(::TracingListCollectionFlux, tracing)
}

/**
 * Extension that wraps MongoDB's [ListDatabasesPublisher] of [T] into an [ListDatabaseFlux] wrapper class
 * @receiver MongoDB's reactivestreams driver list databases operation return class
 * @param T the type of the result
 * @param tracing OpenTracing's Brave instrumentation
 * @return Reactor wrapper for [ListDatabasesPublisher]
 */
public fun <T> ListDatabasesPublisher<T>.toTracingReactor(tracing: Tracing): TracingListDatabaseFlux<T> {
    return intoReactor(::TracingListDatabaseFlux, tracing)
}

/**
 * Extension that wraps MongoDB's [ListIndexesPublisher] of [T] into an [ListIndexesFlux] wrapper class
 * @receiver MongoDB's reactivestreams driver list indexes operation return class
 * @param T the type of the result
 * @param tracing OpenTracing's Brave instrumentation
 * @return Reactor wrapper for [ListIndexesPublisher]
 */
public fun <T> ListIndexesPublisher<T>.toTracingReactor(tracing: Tracing): TracingListIndexesFlux<T> {
    return intoReactor(::TracingListIndexesFlux, tracing)
}

/**
 * Extension that wraps MongoDB's [MapReducePublisher] of [T] into an [MapReduceFlux] wrapper class
 * @receiver MongoDB's reactivestreams driver map-reduce operation return class
 * @param T the type of the result
 * @param tracing OpenTracing's Brave instrumentation
 * @return Reactor wrapper for [MapReducePublisher]
 */
public fun <T> MapReducePublisher<T>.toTracingReactor(tracing: Tracing): TracingMapReduceFlux<T> {
    return intoReactor(::TracingMapReduceFlux, tracing)
}

/**
 * Extension that wraps MongoDB's [MongoCollection] of [T] into an [ReactorMongoCollection] wrapper class
 * @receiver MongoDB's reactivestreams driver collection class
 * @param tracing OpenTracing's Brave instrumentation
 * @return Reactor wrapper for [MongoCollection]
 */
public fun <T> MongoCollection<T>.toTracingReactor(tracing: Tracing): TracingReactorMongoCollection<T> {
    return intoReactor(::TracingReactorMongoCollection, tracing)
}

/**
 * Extension that wraps MongoDB's [MongoDatabase] into an [ReactorMongoDatabase] wrapper class
 * @receiver MongoDB's reactivestreams driver database class
 * @param tracing OpenTracing's Brave instrumentation
 * @return Reactor wrapper for [MongoDatabase]
 */
public fun MongoDatabase.toTracingReactor(tracing: Tracing): TracingReactorMongoDatabase {
    return intoReactor(::TracingReactorMongoDatabase, tracing)
}

/**
 * Extension that wraps MongoDB's [MongoClient] into an [ReactorMongoClient] wrapper class
 * @receiver MongoDB's reactivestreams driver client class
 * @param tracing OpenTracing's Brave instrumentation
 * @return Reactor wrapper for [MongoClient]
 */
public fun MongoClient.toTracingReactor(tracing: Tracing): TracingReactorMongoClient = intoReactor(::TracingReactorMongoClient, tracing)

/**
 * Extension that wraps a Reactor [CoreSubscriber] into a [ScopePassingSpanSubscriber] able to continue an OpenTracing span
 * @receiver Reactor's original subscriber
 * @param tracing OpenTracing's Brave instrumentation
 * @return a subscriber aware of OpenTracing's context that continues spans
 */
internal fun <T> CoreSubscriber<T>.toTracing(tracing: Tracing): CoreSubscriber<T> {
    val context = currentContext()
    val currentTraceContext = tracing.currentTraceContext()
    val parent = traceContext(context, currentTraceContext) ?: return this
    return ScopePassingSpanSubscriber(this, context, currentTraceContext, parent)
}

/**
 * Extension that transform a Reactive Stream's [Publisher] into a Reactor's [Mono] aware of OpenTracing's context
 * @receiver Reactive Stream's [Publisher]
 * @param tracing OpenTracing's Brave instrumentation
 * @return reactor [Mono] that continues OpenTracing spans
 */
internal fun <T> Publisher<T>.toTracingMono(tracing: Tracing): Mono<T> {
    return toMono().transform(scopePassingSpanOperator(tracing))
}

/**
 * Extension that transform a Reactive Stream's [Publisher] into a Reactor's [Flux] aware of OpenTracing's context
 * @receiver Reactive Stream's [Publisher]
 * @param tracing OpenTracing's Brave instrumentation
 * @return reactor [Flux] that continues OpenTracing spans
 */
internal fun <T : Any> Publisher<T>.toTracingFlux(tracing: Tracing): Flux<T> {
    return toFlux().transform(scopePassingSpanOperator(tracing))
}

private inline fun <R, reified T> R.intoReactor(ctor: (R, tracing: Tracing) -> T, tracing: Tracing): T = this as? T ?: ctor(this, tracing)

private fun <T> scopePassingSpanOperator(tracing: Tracing? = null): Function<in Publisher<T>, out Publisher<T>> {
    return Operators.lift { p, sub ->
        if (tracing == null || p is Fuseable.ScalarCallable<*>) return@lift sub
        val context = sub.currentContext()
        val currentTraceContext = tracing.currentTraceContext()
        val parent = traceContext(context, currentTraceContext) ?: return@lift sub
        ScopePassingSpanSubscriber(sub, context, currentTraceContext, parent)
    }
}

private fun traceContext(context: Context, fallback: CurrentTraceContext): TraceContext? {
    return context.getOrDefault(TraceContext::class, fallback.get())
}
