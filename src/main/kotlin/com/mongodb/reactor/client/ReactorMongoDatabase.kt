package com.mongodb.reactor.client

import com.mongodb.ReadConcern
import com.mongodb.ReadPreference
import com.mongodb.WriteConcern
import com.mongodb.client.model.CreateCollectionOptions
import com.mongodb.client.model.CreateViewOptions
import com.mongodb.reactivestreams.client.ClientSession
import com.mongodb.reactivestreams.client.ListCollectionsPublisher
import com.mongodb.reactivestreams.client.MongoDatabase
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistry
import org.bson.conversions.Bson
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono

class ReactorMongoDatabase(private val delegate: MongoDatabase) : MongoDatabase by delegate {

    override fun withCodecRegistry(codecRegistry: CodecRegistry): ReactorMongoDatabase {
        return ReactorMongoDatabase(delegate.withCodecRegistry(codecRegistry))
    }

    override fun withReadPreference(readPreference: ReadPreference): ReactorMongoDatabase {
        return ReactorMongoDatabase(delegate.withReadPreference(readPreference))
    }

    override fun withWriteConcern(writeConcern: WriteConcern): MongoDatabase = ReactorMongoDatabase(delegate.withWriteConcern(writeConcern))

    override fun withReadConcern(readConcern: ReadConcern): MongoDatabase = ReactorMongoDatabase(delegate.withReadConcern(readConcern))

    override fun getCollection(collectionName: String): ReactorMongoCollection<Document> {
        return ReactorMongoCollection(delegate.getCollection(collectionName))
    }

    override fun <TDocument : Any> getCollection(collectionName: String, clazz: Class<TDocument>): ReactorMongoCollection<TDocument> {
        return ReactorMongoCollection(delegate.getCollection(collectionName, clazz))
    }

    override fun runCommand(command: Bson): Mono<Document> = delegate.runCommand(command).toMono()

    override fun runCommand(command: Bson, readPreference: ReadPreference): Mono<Document> {
        return delegate.runCommand(command, readPreference).toMono()
    }

    override fun <TResult : Any> runCommand(command: Bson, clazz: Class<TResult>): Mono<TResult> {
        return delegate.runCommand(command, clazz).toMono()
    }

    override fun <TResult : Any> runCommand(command: Bson, readPreference: ReadPreference, clazz: Class<TResult>): Mono<TResult> {
        return delegate.runCommand(command, readPreference, clazz).toMono()
    }

    override fun runCommand(clientSession: ClientSession, command: Bson): Mono<Document> {
        return delegate.runCommand(clientSession, command).toMono()
    }

    override fun runCommand(clientSession: ClientSession, command: Bson, readPreference: ReadPreference): Mono<Document> {
        return delegate.runCommand(clientSession, command, readPreference).toMono()
    }

    override fun <TResult : Any> runCommand(clientSession: ClientSession, command: Bson, clazz: Class<TResult>): Mono<TResult> {
        return delegate.runCommand(clientSession, command, clazz).toMono()
    }

    override fun <TResult : Any> runCommand(
        clientSession: ClientSession,
        command: Bson,
        readPreference: ReadPreference,
        clazz: Class<TResult>
    ): Mono<TResult> {
        return delegate.runCommand(clientSession, command, readPreference, clazz).toMono()
    }

    override fun drop(): Mono<Void> = delegate.drop().toMono()

    override fun drop(clientSession: ClientSession): Mono<Void> = delegate.drop(clientSession).toMono()

    override fun listCollectionNames(): Flux<String> = delegate.listCollectionNames().toFlux()

    override fun listCollectionNames(clientSession: ClientSession): Flux<String> = delegate.listCollectionNames(clientSession).toFlux()

    override fun listCollections(): ListCollectionsPublisher<Document> = delegate.listCollections()

    override fun <TResult : Any> listCollections(clazz: Class<TResult>): ListCollectionsPublisher<TResult> = delegate.listCollections(clazz)

    override fun listCollections(clientSession: ClientSession): ListCollectionsPublisher<Document> = delegate.listCollections(clientSession)

    override fun <TResult : Any> listCollections(
        clientSession: ClientSession,
        clazz: Class<TResult>
    ): ListCollectionsPublisher<TResult> {
        return delegate.listCollections(clientSession, clazz)
    }

    override fun createCollection(collectionName: String): Mono<Void> = delegate.createCollection(collectionName).toMono()

    override fun createCollection(collectionName: String, options: CreateCollectionOptions): Mono<Void> {
        return delegate.createCollection(collectionName, options).toMono()
    }

    override fun createCollection(clientSession: ClientSession, collectionName: String): Mono<Void> {
        return delegate.createCollection(clientSession, collectionName).toMono()
    }

    override fun createCollection(
        clientSession: ClientSession,
        collectionName: String,
        options: CreateCollectionOptions
    ): Mono<Void> {
        return delegate.createCollection(clientSession, collectionName, options).toMono()
    }

    override fun createView(viewName: String, viewOn: String, pipeline: MutableList<out Bson>): Mono<Void> {
        return delegate.createView(viewName, viewOn, pipeline).toMono()
    }

    override fun createView(
        viewName: String,
        viewOn: String,
        pipeline: MutableList<out Bson>,
        createViewOptions: CreateViewOptions
    ): Mono<Void> {
        return delegate.createView(viewName, viewOn, pipeline, createViewOptions).toMono()
    }

    override fun createView(
        clientSession: ClientSession,
        viewName: String,
        viewOn: String,
        pipeline: MutableList<out Bson>
    ): Mono<Void> {
        return delegate.createView(clientSession, viewName, viewOn, pipeline).toMono()
    }

    override fun createView(
        clientSession: ClientSession,
        viewName: String,
        viewOn: String,
        pipeline: MutableList<out Bson>,
        createViewOptions: CreateViewOptions
    ): Mono<Void> {
        return delegate.createView(clientSession, viewName, viewOn, pipeline, createViewOptions).toMono()
    }

    override fun watch(): ChangeStreamFlux<Document> = delegate.watch().toReactor()

    override fun <TResult : Any> watch(resultClass: Class<TResult>): ChangeStreamFlux<TResult> = delegate.watch(resultClass).toReactor()

    override fun watch(pipeline: MutableList<out Bson>): ChangeStreamFlux<Document> = delegate.watch(pipeline).toReactor()

    override fun <TResult : Any> watch(pipeline: MutableList<out Bson>, resultClass: Class<TResult>): ChangeStreamFlux<TResult> {
        return delegate.watch(pipeline, resultClass).toReactor()
    }

    override fun watch(clientSession: ClientSession): ChangeStreamFlux<Document> = delegate.watch(clientSession).toReactor()

    override fun <TResult : Any> watch(clientSession: ClientSession, resultClass: Class<TResult>): ChangeStreamFlux<TResult> {
        return delegate.watch(clientSession, resultClass).toReactor()
    }

    override fun watch(clientSession: ClientSession, pipeline: MutableList<out Bson>): ChangeStreamFlux<Document> {
        return delegate.watch(clientSession, pipeline).toReactor()
    }

    override fun <TResult : Any> watch(
        clientSession: ClientSession,
        pipeline: MutableList<out Bson>,
        resultClass: Class<TResult>
    ): ChangeStreamFlux<TResult> {
        return delegate.watch(clientSession, pipeline, resultClass).toReactor()
    }

    override fun aggregate(pipeline: MutableList<out Bson>): AggregateFlux<Document> = delegate.aggregate(pipeline).toReactor()

    override fun <TResult : Any> aggregate(pipeline: MutableList<out Bson>, resultClass: Class<TResult>): AggregateFlux<TResult> {
        return delegate.aggregate(pipeline, resultClass).toReactor()
    }

    override fun aggregate(clientSession: ClientSession, pipeline: MutableList<out Bson>): AggregateFlux<Document> {
        return delegate.aggregate(clientSession, pipeline).toReactor()
    }

    override fun <TResult : Any> aggregate(
        clientSession: ClientSession,
        pipeline: MutableList<out Bson>,
        resultClass: Class<TResult>
    ): AggregateFlux<TResult> {
        return delegate.aggregate(clientSession, pipeline, resultClass).toReactor()
    }
}
