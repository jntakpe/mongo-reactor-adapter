package com.mongodb.reactor.client.tracing

import brave.Tracing
import com.mongodb.reactivestreams.client.ListCollectionsPublisher
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyAll
import org.bson.BsonDocument
import org.bson.Document
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Operators
import reactor.kotlin.test.test
import java.util.concurrent.TimeUnit

internal class TracingListCollectionFluxTest {

    private val tracing = mockk<Tracing>(relaxed = true)
    private val publisher = mockk<ListCollectionsPublisher<Document>>(relaxed = true)
    private val tracingFlux = spyk(TracingListCollectionFlux(publisher, tracing))
    private val database = MongoContainer.database.toTracingReactor(tracing)

    @BeforeEach
    fun init() {
        MongoContainer.initData()
    }

    @AfterEach
    fun clean() {
        MongoContainer.clearData()
    }

    @Test
    fun `tracing list collection flux should always delegate method calls to the underlying publisher`() {
        val document = BsonDocument()
        tracingFlux.filter(document)
        tracingFlux.maxTime(10, TimeUnit.SECONDS)
        tracingFlux.batchSize(10)
        tracingFlux.first()
        verifyAll {
            publisher.filter(document)
            publisher.maxTime(10, TimeUnit.SECONDS)
            publisher.batchSize(10)
            publisher.first()
        }
        confirmVerified(publisher)
    }

    @Test
    fun `tracing list collection flux should emit items`() {
        database
            .listCollections()
            .map { it["name"] }
            .test()
            .expectSubscription()
            .expectNext(MongoContainer.COLLECTION_NAME)
            .verifyComplete()
    }

    @Test
    fun `tracing list collection flux using dedicated subscriber`() {
        tracingFlux.subscribe(Operators.emptySubscriber())
        verify {
            publisher.subscribe(match { it is ScopePassingSpanSubscriber })
        }
        confirmVerified(publisher)
    }
}
