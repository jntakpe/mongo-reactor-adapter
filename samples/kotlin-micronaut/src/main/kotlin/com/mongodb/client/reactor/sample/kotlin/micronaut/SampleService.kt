package com.mongodb.client.reactor.sample.kotlin.micronaut

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactor.client.ReactorMongoClient
import com.mongodb.reactor.client.ReactorMongoCollection
import com.mongodb.reactor.client.ReactorMongoDatabase
import com.mongodb.reactor.client.toReactor
import reactor.core.publisher.Mono
import javax.inject.Singleton

@Singleton
class SampleService(reactiveStreamsClient: MongoClient) {

    private val reactorClient: ReactorMongoClient = reactiveStreamsClient.toReactor()

    // or private val reactorDb = reactiveStreamsClient.getDatabase("test").toReactor()
    private val reactorDb: ReactorMongoDatabase = reactorClient.getDatabase("test")

    // or private val reactorCollection = reactiveStreamsClient.getDatabase("test").getCollection("users", User::class.java).toReactor()
    private val reactorCollection: ReactorMongoCollection<User> = reactorDb.getCollection("users", User::class.java)

    fun listDatabasesNames() = reactorClient.listDatabaseNames()

    fun listCollections() = reactorDb.listCollectionNames()

    fun findAll() = reactorCollection.find()

    fun create(user: User): Mono<User> = reactorCollection.insertOne(user).thenReturn(user)
}
