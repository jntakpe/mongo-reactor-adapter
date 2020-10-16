package com.mongodb.reactor.client

import com.mongodb.ClientSessionOptions
import com.mongodb.connection.ClusterDescription
import com.mongodb.reactivestreams.client.ClientSession
import com.mongodb.reactivestreams.client.MongoClient
import org.bson.Document
import org.bson.conversions.Bson
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono

public class ReactorMongoClient(private val delegate: MongoClient) : MongoClient by delegate {

    override fun getDatabase(name: String): ReactorMongoDatabase = ReactorMongoDatabase(delegate.getDatabase(name))

    override fun listDatabaseNames(): Flux<String> = delegate.listDatabaseNames().toFlux()

    override fun listDatabaseNames(clientSession: ClientSession): Flux<String> = delegate.listDatabaseNames(clientSession).toFlux()

    override fun listDatabases(): ListDatabaseFlux<Document> = delegate.listDatabases().toReactor()

    override fun <TResult : Any> listDatabases(clazz: Class<TResult>): ListDatabaseFlux<TResult> {
        return delegate.listDatabases(clazz).toReactor()
    }

    override fun listDatabases(clientSession: ClientSession): ListDatabaseFlux<Document> {
        return delegate.listDatabases(clientSession).toReactor()
    }

    override fun <TResult : Any> listDatabases(clientSession: ClientSession, clazz: Class<TResult>): ListDatabaseFlux<TResult> {
        return delegate.listDatabases(clientSession, clazz).toReactor()
    }

    override fun watch(): ChangeStreamFlux<Document> = delegate.watch().toReactor()

    override fun <TResult : Any> watch(resultClass: Class<TResult>): ChangeStreamFlux<TResult> = delegate.watch(resultClass).toReactor()

    override fun watch(pipeline: List<Bson>): ChangeStreamFlux<Document> = delegate.watch(pipeline).toReactor()

    override fun <TResult : Any> watch(pipeline: List<Bson>, resultClass: Class<TResult>): ChangeStreamFlux<TResult> {
        return delegate.watch(pipeline, resultClass).toReactor()
    }

    override fun watch(clientSession: ClientSession): ChangeStreamFlux<Document> = delegate.watch(clientSession).toReactor()

    override fun <TResult : Any> watch(clientSession: ClientSession, resultClass: Class<TResult>): ChangeStreamFlux<TResult> {
        return delegate.watch(clientSession, resultClass).toReactor()
    }

    override fun watch(clientSession: ClientSession, pipeline: List<Bson>): ChangeStreamFlux<Document> {
        return delegate.watch(clientSession, pipeline).toReactor()
    }

    override fun <TResult : Any> watch(
        clientSession: ClientSession,
        pipeline: List<Bson>,
        resultClass: Class<TResult>
    ): ChangeStreamFlux<TResult> {
        return delegate.watch(clientSession, pipeline, resultClass).toReactor()
    }

    override fun startSession(): Mono<ClientSession> = delegate.startSession().toMono()

    override fun startSession(options: ClientSessionOptions): Mono<ClientSession> = delegate.startSession(options).toMono()

    override fun getClusterDescription(): ClusterDescription = delegate.clusterDescription
}
