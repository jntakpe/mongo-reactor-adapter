package com.mongodb.reactor.client.tracing

import brave.Tracing
import com.mongodb.ClientSessionOptions
import com.mongodb.connection.ClusterDescription
import com.mongodb.reactivestreams.client.ClientSession
import com.mongodb.reactivestreams.client.MongoClient
import org.bson.Document
import org.bson.conversions.Bson
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Wraps MongoDB's reactivestreams driver [MongoClient] class.
 * Delegates every call to the original MongoDB's reactivestreams driver class but transform ReactiveStream's Publisher return types
 * into either [Flux] or [Mono] subscribing with an OpenTracing aware subscriber that continues spans.
 * @param delegate original MongoDB's reactivestreams driver class to which all the calls are delegated
 * @param tracing OpenTracing's instrumentation
 * @see MongoClient
 */
public class TracingReactorMongoClient(private val delegate: MongoClient, private val tracing: Tracing) : MongoClient by delegate {

    override fun getDatabase(name: String): TracingReactorMongoDatabase = TracingReactorMongoDatabase(delegate.getDatabase(name), tracing)

    override fun listDatabaseNames(): Flux<String> = delegate.listDatabaseNames().toTracingFlux(tracing)

    override fun listDatabaseNames(clientSession: ClientSession): Flux<String> {
        return delegate.listDatabaseNames(clientSession).toTracingFlux(tracing)
    }

    override fun listDatabases(): TracingListDatabaseFlux<Document> = delegate.listDatabases().toTracingReactor(tracing)

    override fun <TResult : Any> listDatabases(clazz: Class<TResult>): TracingListDatabaseFlux<TResult> {
        return delegate.listDatabases(clazz).toTracingReactor(tracing)
    }

    override fun listDatabases(clientSession: ClientSession): TracingListDatabaseFlux<Document> {
        return delegate.listDatabases(clientSession).toTracingReactor(tracing)
    }

    override fun <TResult : Any> listDatabases(clientSession: ClientSession, clazz: Class<TResult>): TracingListDatabaseFlux<TResult> {
        return delegate.listDatabases(clientSession, clazz).toTracingReactor(tracing)
    }

    override fun watch(): TracingChangeStreamFlux<Document> = delegate.watch().toTracingReactor(tracing)

    override fun <TResult : Any> watch(resultClass: Class<TResult>): TracingChangeStreamFlux<TResult> {
        return delegate.watch(resultClass).toTracingReactor(tracing)
    }

    override fun watch(pipeline: List<Bson>): TracingChangeStreamFlux<Document> = delegate.watch(pipeline).toTracingReactor(tracing)

    override fun <TResult : Any> watch(pipeline: List<Bson>, resultClass: Class<TResult>): TracingChangeStreamFlux<TResult> {
        return delegate.watch(pipeline, resultClass).toTracingReactor(tracing)
    }

    override fun watch(clientSession: ClientSession): TracingChangeStreamFlux<Document> {
        return delegate.watch(clientSession).toTracingReactor(tracing)
    }

    override fun <TResult : Any> watch(clientSession: ClientSession, resultClass: Class<TResult>): TracingChangeStreamFlux<TResult> {
        return delegate.watch(clientSession, resultClass).toTracingReactor(tracing)
    }

    override fun watch(clientSession: ClientSession, pipeline: List<Bson>): TracingChangeStreamFlux<Document> {
        return delegate.watch(clientSession, pipeline).toTracingReactor(tracing)
    }

    override fun <TResult : Any> watch(
        clientSession: ClientSession,
        pipeline: List<Bson>,
        resultClass: Class<TResult>,
    ): TracingChangeStreamFlux<TResult> {
        return delegate.watch(clientSession, pipeline, resultClass).toTracingReactor(tracing)
    }

    override fun startSession(): Mono<ClientSession> = delegate.startSession().toTracingMono(tracing)

    override fun startSession(options: ClientSessionOptions): Mono<ClientSession> = delegate.startSession(options).toTracingMono(tracing)

    override fun getClusterDescription(): ClusterDescription = delegate.clusterDescription
}
