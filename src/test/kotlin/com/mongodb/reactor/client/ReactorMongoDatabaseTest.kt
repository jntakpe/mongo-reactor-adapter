package com.mongodb.reactor.client

import com.mongodb.ReadConcern
import com.mongodb.ReadPreference
import com.mongodb.WriteConcern
import com.mongodb.client.model.CreateCollectionOptions
import com.mongodb.client.model.CreateViewOptions
import com.mongodb.reactivestreams.client.ClientSession
import com.mongodb.reactivestreams.client.MongoDatabase
import com.mongodb.reactor.client.MongoContainer.COLLECTION_NAME
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifyAll
import org.bson.BsonDocument
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistry
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.kotlin.test.test

internal class ReactorMongoDatabaseTest {

    private val original = mockk<MongoDatabase>(relaxed = true)
    private val reactor = spyk(ReactorMongoDatabase(original))
    private val database = MongoContainer.database.toReactor()

    @BeforeEach
    fun init() {
        MongoContainer.initData()
    }

    @AfterEach
    fun clean() {
        MongoContainer.clearData()
    }

    @Test
    fun `reactor database should always delegate method calls to the underlying database`() {
        val registry = mockk<CodecRegistry>(relaxed = true)
        reactor.withCodecRegistry(registry)
        reactor.withReadPreference(ReadPreference.nearest())
        reactor.withWriteConcern(WriteConcern.W1)
        reactor.withReadConcern(ReadConcern.LOCAL)
        reactor.getCollection(COLLECTION_NAME)
        reactor.getCollection(COLLECTION_NAME, Document::class.java)
        val document = BsonDocument()
        reactor.runCommand(document)
        reactor.runCommand(document, ReadPreference.nearest())
        reactor.runCommand(document, Document::class.java)
        reactor.runCommand(document, ReadPreference.nearest(), Document::class.java)
        val session = mockk<ClientSession>(relaxed = true)
        reactor.runCommand(session, document)
        reactor.runCommand(session, document, ReadPreference.nearest())
        reactor.runCommand(session, document, Document::class.java)
        reactor.runCommand(session, document, ReadPreference.nearest(), Document::class.java)
        reactor.drop()
        reactor.drop(session)
        reactor.listCollectionNames()
        reactor.listCollectionNames(session)
        reactor.listCollections()
        reactor.listCollections(Document::class.java)
        reactor.listCollections(session)
        reactor.listCollections(session, Document::class.java)
        reactor.createCollection(COLLECTION_NAME)
        val collectionOptions = CreateCollectionOptions()
        reactor.createCollection(COLLECTION_NAME, collectionOptions)
        reactor.createCollection(session, COLLECTION_NAME)
        reactor.createCollection(session, COLLECTION_NAME, collectionOptions)
        val view = "partialView"
        val usernameAttribute = "username"
        val pipeline = listOf(document)
        reactor.createView(view, usernameAttribute, pipeline)
        val viewOptions = CreateViewOptions()
        reactor.createView(view, usernameAttribute, pipeline, viewOptions)
        reactor.createView(session, view, usernameAttribute, pipeline)
        reactor.createView(session, view, usernameAttribute, pipeline, viewOptions)
        reactor.watch()
        reactor.watch(Document::class.java)
        reactor.watch(pipeline)
        reactor.watch(pipeline, Document::class.java)
        reactor.watch(session)
        reactor.watch(session, Document::class.java)
        reactor.watch(session, pipeline)
        reactor.watch(session, pipeline, Document::class.java)
        reactor.aggregate(pipeline)
        reactor.aggregate(pipeline, Document::class.java)
        reactor.aggregate(session, pipeline)
        reactor.aggregate(session, pipeline, Document::class.java)
        verifyAll {
            original.withCodecRegistry(registry)
            original.withReadPreference(ReadPreference.nearest())
            original.withWriteConcern(WriteConcern.W1)
            original.withReadConcern(ReadConcern.LOCAL)
            original.getCollection(COLLECTION_NAME)
            original.getCollection(COLLECTION_NAME, Document::class.java)
            original.runCommand(document)
            original.runCommand(document, ReadPreference.nearest())
            original.runCommand(document, Document::class.java)
            original.runCommand(document, ReadPreference.nearest(), Document::class.java)
            original.runCommand(session, document)
            original.runCommand(session, document, ReadPreference.nearest())
            original.runCommand(session, document, Document::class.java)
            original.runCommand(session, document, ReadPreference.nearest(), Document::class.java)
            original.drop()
            original.drop(session)
            original.listCollectionNames()
            original.listCollectionNames(session)
            original.listCollections()
            original.listCollections(Document::class.java)
            original.listCollections(session)
            original.listCollections(session, Document::class.java)
            original.createCollection(COLLECTION_NAME)
            original.createCollection(COLLECTION_NAME, collectionOptions)
            original.createCollection(session, COLLECTION_NAME)
            original.createCollection(session, COLLECTION_NAME, collectionOptions)
            original.createView(view, usernameAttribute, pipeline)
            original.createView(view, usernameAttribute, pipeline, viewOptions)
            original.createView(session, view, usernameAttribute, pipeline)
            original.createView(session, view, usernameAttribute, pipeline, viewOptions)
            original.watch()
            original.watch(Document::class.java)
            original.watch(pipeline)
            original.watch(pipeline, Document::class.java)
            original.watch(session)
            original.watch(session, Document::class.java)
            original.watch(session, pipeline)
            original.watch(session, pipeline, Document::class.java)
            original.aggregate(pipeline)
            original.aggregate(pipeline, Document::class.java)
            original.aggregate(session, pipeline)
            original.aggregate(session, pipeline, Document::class.java)
        }
        confirmVerified(original)
    }

    @Test
    fun `reactor database should emit items`() {
        database
            .listCollectionNames()
            .test()
            .expectSubscription()
            .expectNext(COLLECTION_NAME)
            .verifyComplete()
    }
}
