package com.mongodb.reactor.client.tracing

import brave.Tracing
import com.mongodb.client.model.Collation
import com.mongodb.client.model.changestream.FullDocument
import com.mongodb.reactivestreams.client.ChangeStreamPublisher
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyAll
import org.bson.BsonDocument
import org.bson.BsonTimestamp
import org.bson.Document
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Operators
import reactor.kotlin.core.publisher.toMono
import reactor.kotlin.test.test
import java.time.Duration
import java.util.concurrent.TimeUnit

internal class TracingChangeStreamFluxTest {

    private val tracing = mockk<Tracing>(relaxed = true)
    private val publisher = mockk<ChangeStreamPublisher<Document>>(relaxed = true)
    private val tracingFlux = spyk(TracingChangeStreamFlux(publisher, tracing))
    private val collection = MongoContainer.database.getCollection(MongoContainer.COLLECTION_NAME)

    @BeforeEach
    fun init() {
        MongoContainer.initData()
    }

    @AfterEach
    fun clean() {
        MongoContainer.clearData()
    }

    @Test
    fun `tracing change stream flux should always delegate method calls to the underlying publisher`() {
        tracingFlux.fullDocument(FullDocument.DEFAULT)
        val document = BsonDocument()
        tracingFlux.resumeAfter(document)
        val timestamp = BsonTimestamp()
        tracingFlux.startAtOperationTime(timestamp)
        tracingFlux.startAfter(document)
        tracingFlux.maxAwaitTime(10L, TimeUnit.SECONDS)
        val collation = Collation.builder().build()
        tracingFlux.collation(collation)
        tracingFlux.withDocumentClass(String::class.java)
        tracingFlux.batchSize(10)
        tracingFlux.first()
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
        }
        confirmVerified(publisher)
    }

    @Test
    fun `tracing change stream flux should emit items`() {
        val realName = "Real"
        val real = Document("username", realName).append("city", "Berlin")
        collection
            .insertOne(real)
            .toMono()
            .delaySubscription(Duration.ofSeconds(1))
            .subscribe()
        collection
            .toTracingReactor(tracing)
            .watch(Document::class.java)
            .map { it.fullDocument?.get("username", "Default") }
            .take(1)
            .test()
            .expectNext(realName)
            .verifyComplete()
    }

    @Test
    fun `tracing change stream flux using dedicated subscriber`() {
        tracingFlux.subscribe(Operators.emptySubscriber())
        verify {
            publisher.subscribe(match { it is ScopePassingSpanSubscriber })
        }
        confirmVerified(publisher)
    }
}
