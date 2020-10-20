package com.mongodb.reactor.client

import com.mongodb.client.model.Collation
import com.mongodb.client.model.changestream.ChangeStreamDocument
import com.mongodb.client.model.changestream.FullDocument
import com.mongodb.reactivestreams.client.ChangeStreamPublisher
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifyAll
import org.bson.BsonDocument
import org.bson.BsonTimestamp
import org.bson.Document
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.DirectProcessor
import reactor.kotlin.core.publisher.toMono
import reactor.kotlin.test.test
import java.time.Duration
import java.util.concurrent.TimeUnit

internal class ChangeStreamFluxTest {

    private val publisher = mockk<ChangeStreamPublisher<Document>>(relaxed = true)
    private val flux = spyk(ChangeStreamFlux(publisher))
    private val collection = MongoContainer.database.getCollection(MongoContainer.COLLECTION_NAME).toReactor()

    @BeforeEach
    fun init() {
        MongoContainer.initData()
    }

    @AfterEach
    fun clean() {
        MongoContainer.clearData()
    }

    @Test
    fun `change stream flux should always delegate method calls to the underlying publisher`() {
        flux.fullDocument(FullDocument.DEFAULT)
        val document = BsonDocument()
        flux.resumeAfter(document)
        val timestamp = BsonTimestamp()
        flux.startAtOperationTime(timestamp)
        flux.startAfter(document)
        flux.maxAwaitTime(10L, TimeUnit.SECONDS)
        val collation = Collation.builder().build()
        flux.collation(collation)
        flux.withDocumentClass(String::class.java)
        flux.batchSize(10)
        flux.first()
        val processor = DirectProcessor.create<ChangeStreamDocument<Document>>()
        flux.subscribe(processor)
        verifyAll {
            publisher.fullDocument(FullDocument.DEFAULT)
            publisher.resumeAfter(document)
            publisher.startAtOperationTime(timestamp)
            publisher.startAfter(document)
            publisher.maxAwaitTime(10L, TimeUnit.SECONDS)
            publisher.collation(collation)
            publisher.withDocumentClass(String::class.java)
            publisher.batchSize(10)
            publisher.first()
            publisher.subscribe(processor)
        }
        confirmVerified(publisher)
    }

    @Test
    fun `change stream flux should emit items`() {
        val realName = "Real"
        val real = Document("username", realName).append("city", "Berlin")
        collection
            .insertOne(real)
            .toMono()
            .delaySubscription(Duration.ofSeconds(1))
            .subscribe()
        collection
            .watch(Document::class.java)
            .map { it.fullDocument?.get("username", "Default") }
            .take(1)
            .test()
            .expectNext(realName)
            .verifyComplete()
    }
}
