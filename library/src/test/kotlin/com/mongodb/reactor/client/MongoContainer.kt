package com.mongodb.reactor.client

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import com.mongodb.reactivestreams.client.MongoDatabase
import org.bson.Document
import org.testcontainers.containers.MongoDBContainer
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono

internal object MongoContainer {

    const val DATABASE_NAME = "test"
    const val COLLECTION_NAME = "people"
    private const val MONGO_DOCKER_IMAGE = "mongo"
    private const val MONGO_VERSION = "4.4"
    private const val MONGO_PROTOCOL = "mongodb://"
    val client: MongoClient = run {
        val container = MongoDBContainer("$MONGO_DOCKER_IMAGE:$MONGO_VERSION").apply { start() }
        MongoClients.create("$MONGO_PROTOCOL${container.containerIpAddress}:${container.firstMappedPort}")
    }
    val database: MongoDatabase = client.getDatabase(DATABASE_NAME)

    fun initData() {
        val jean = Document("username", "Jean").append("city", "Paris").append("age", 30)
        val pierre = Document("username", "Pierre").append("city", "Paris").append("age", 45)
        val tom = Document("username", "Tom").append("city", "London").append("age", 35)
        database.getCollection(COLLECTION_NAME)
            .insertMany(listOf(jean, pierre, tom))
            .toFlux()
            .blockLast()
    }

    fun clearData() {
        database.getCollection(COLLECTION_NAME)
            .drop()
            .toMono()
            .block()
    }
}
