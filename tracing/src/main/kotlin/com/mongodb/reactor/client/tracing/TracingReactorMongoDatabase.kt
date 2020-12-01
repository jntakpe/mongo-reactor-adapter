package com.mongodb.reactor.client.tracing

import brave.Tracing
import com.mongodb.ReadConcern
import com.mongodb.ReadPreference
import com.mongodb.WriteConcern
import com.mongodb.client.model.CreateCollectionOptions
import com.mongodb.client.model.CreateViewOptions
import com.mongodb.reactivestreams.client.ClientSession
import com.mongodb.reactivestreams.client.MongoDatabase
import com.mongodb.reactor.client.ReactorMongoDatabase
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistry
import org.bson.conversions.Bson
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

/**
 * Wraps MongoDB's reactivestreams driver [ReactorMongoDatabase] class.
 * Delegates every call to the original MongoDB's reactivestreams driver class but transform ReactiveStream's Publisher return types
 * into either [Flux] or [Mono]  subscribing with an OpenTracing aware subscriber that continues spans.
 * @param delegate original MongoDB's reactivestreams driver class to which all the calls are delegated
 * @param tracing OpenTracing's instrumentation
 * @see ReactorMongoDatabase
 */
public class TracingReactorMongoDatabase(private val delegate: MongoDatabase, private val tracing: Tracing) : MongoDatabase by delegate {

    override fun withCodecRegistry(codecRegistry: CodecRegistry): TracingReactorMongoDatabase {
        return TracingReactorMongoDatabase(delegate.withCodecRegistry(codecRegistry), tracing)
    }

    override fun withReadPreference(readPreference: ReadPreference): TracingReactorMongoDatabase {
        return TracingReactorMongoDatabase(delegate.withReadPreference(readPreference), tracing)
    }

    override fun withWriteConcern(writeConcern: WriteConcern): TracingReactorMongoDatabase {
        return TracingReactorMongoDatabase(delegate.withWriteConcern(writeConcern), tracing)
    }

    override fun withReadConcern(readConcern: ReadConcern): TracingReactorMongoDatabase {
        return TracingReactorMongoDatabase(delegate.withReadConcern(readConcern), tracing)
    }

    override fun getCollection(collectionName: String): TracingReactorMongoCollection<Document> {
        return TracingReactorMongoCollection(delegate.getCollection(collectionName), tracing)
    }

    override fun <TDocument : Any> getCollection(
        collectionName: String,
        clazz: Class<TDocument>,
    ): TracingReactorMongoCollection<TDocument> {
        return TracingReactorMongoCollection(delegate.getCollection(collectionName, clazz), tracing)
    }

    override fun runCommand(command: Bson): Mono<Document> = delegate.runCommand(command).toTracingMono(tracing)

    override fun runCommand(command: Bson, readPreference: ReadPreference): Mono<Document> {
        return delegate.runCommand(command, readPreference).toTracingMono(tracing)
    }

    override fun <TResult : Any> runCommand(command: Bson, clazz: Class<TResult>): Mono<TResult> {
        return delegate.runCommand(command, clazz).toMono().toTracingMono(tracing)
    }

    override fun <TResult : Any> runCommand(command: Bson, readPreference: ReadPreference, clazz: Class<TResult>): Mono<TResult> {
        return delegate.runCommand(command, readPreference, clazz).toTracingMono(tracing)
    }

    override fun runCommand(clientSession: ClientSession, command: Bson): Mono<Document> {
        return delegate.runCommand(clientSession, command).toTracingMono(tracing)
    }

    override fun runCommand(clientSession: ClientSession, command: Bson, readPreference: ReadPreference): Mono<Document> {
        return delegate.runCommand(clientSession, command, readPreference).toTracingMono(tracing)
    }

    override fun <TResult : Any> runCommand(clientSession: ClientSession, command: Bson, clazz: Class<TResult>): Mono<TResult> {
        return delegate.runCommand(clientSession, command, clazz).toTracingMono(tracing)
    }

    override fun <TResult : Any> runCommand(
        clientSession: ClientSession,
        command: Bson,
        readPreference: ReadPreference,
        clazz: Class<TResult>,
    ): Mono<TResult> {
        return delegate.runCommand(clientSession, command, readPreference, clazz).toTracingMono(tracing)
    }

    override fun drop(): Mono<Void> = delegate.drop().toTracingMono(tracing)

    override fun drop(clientSession: ClientSession): Mono<Void> = delegate.drop(clientSession).toTracingMono(tracing)

    override fun listCollectionNames(): Flux<String> = delegate.listCollectionNames().toTracingFlux(tracing)

    override fun listCollectionNames(clientSession: ClientSession): Flux<String> {
        return delegate.listCollectionNames(clientSession).toTracingFlux(tracing)
    }

    override fun listCollections(): TracingListCollectionFlux<Document> = delegate.listCollections().toTracingReactor(tracing)

    override fun <TResult : Any> listCollections(clazz: Class<TResult>): TracingListCollectionFlux<TResult> {
        return delegate.listCollections(clazz).toTracingReactor(tracing)
    }

    override fun listCollections(clientSession: ClientSession): TracingListCollectionFlux<Document> {
        return delegate.listCollections(clientSession).toTracingReactor(tracing)
    }

    override fun <TResult : Any> listCollections(
        clientSession: ClientSession,
        clazz: Class<TResult>,
    ): TracingListCollectionFlux<TResult> {
        return delegate.listCollections(clientSession, clazz).toTracingReactor(tracing)
    }

    override fun createCollection(collectionName: String): Mono<Void> = delegate.createCollection(collectionName).toTracingMono(tracing)

    override fun createCollection(collectionName: String, options: CreateCollectionOptions): Mono<Void> {
        return delegate.createCollection(collectionName, options).toTracingMono(tracing)
    }

    override fun createCollection(clientSession: ClientSession, collectionName: String): Mono<Void> {
        return delegate.createCollection(clientSession, collectionName).toTracingMono(tracing)
    }

    override fun createCollection(
        clientSession: ClientSession,
        collectionName: String,
        options: CreateCollectionOptions,
    ): Mono<Void> {
        return delegate.createCollection(clientSession, collectionName, options).toTracingMono(tracing)
    }

    override fun createView(viewName: String, viewOn: String, pipeline: List<Bson>): Mono<Void> {
        return delegate.createView(viewName, viewOn, pipeline).toTracingMono(tracing)
    }

    override fun createView(
        viewName: String,
        viewOn: String,
        pipeline: List<Bson>,
        createViewOptions: CreateViewOptions,
    ): Mono<Void> {
        return delegate.createView(viewName, viewOn, pipeline, createViewOptions).toTracingMono(tracing)
    }

    override fun createView(
        clientSession: ClientSession,
        viewName: String,
        viewOn: String,
        pipeline: List<Bson>,
    ): Mono<Void> {
        return delegate.createView(clientSession, viewName, viewOn, pipeline).toTracingMono(tracing)
    }

    override fun createView(
        clientSession: ClientSession,
        viewName: String,
        viewOn: String,
        pipeline: List<Bson>,
        createViewOptions: CreateViewOptions,
    ): Mono<Void> {
        return delegate.createView(clientSession, viewName, viewOn, pipeline, createViewOptions).toTracingMono(tracing)
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

    override fun aggregate(pipeline: List<Bson>): TracingAggregateFlux<Document> = delegate.aggregate(pipeline).toTracingReactor(tracing)

    override fun <TResult : Any> aggregate(pipeline: List<Bson>, resultClass: Class<TResult>): TracingAggregateFlux<TResult> {
        return delegate.aggregate(pipeline, resultClass).toTracingReactor(tracing)
    }

    override fun aggregate(clientSession: ClientSession, pipeline: List<Bson>): TracingAggregateFlux<Document> {
        return delegate.aggregate(clientSession, pipeline).toTracingReactor(tracing)
    }

    override fun <TResult : Any> aggregate(
        clientSession: ClientSession,
        pipeline: List<Bson>,
        resultClass: Class<TResult>,
    ): TracingAggregateFlux<TResult> {
        return delegate.aggregate(clientSession, pipeline, resultClass).toTracingReactor(tracing)
    }
}
