package com.mongodb.reactor.client.tracing

import brave.Tracing
import com.mongodb.reactivestreams.client.ListIndexesPublisher
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyAll
import org.bson.Document
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Operators
import reactor.kotlin.test.test
import java.util.concurrent.TimeUnit

internal class TracingListIndexesFluxTest {

    private val tracing = mockk<Tracing>(relaxed = true)
    private val publisher = mockk<ListIndexesPublisher<Document>>(relaxed = true)
    private val tracingFlux = spyk(TracingListIndexesFlux(publisher, tracing))
    private val collection = MongoContainer.database.getCollection(MongoContainer.COLLECTION_NAME).toTracingReactor(tracing)

    @BeforeEach
    fun init() {
        MongoContainer.initData()
    }

    @AfterEach
    fun clean() {
        MongoContainer.clearData()
    }

    @Test
    fun `tracing list indexes should always delegate method calls to the underlying publisher`() {
        tracingFlux.maxTime(10, TimeUnit.SECONDS)
        tracingFlux.batchSize(10)
        tracingFlux.first()
        verifyAll {
            publisher.maxTime(10, TimeUnit.SECONDS)
            publisher.batchSize(10)
            publisher.first()
        }
        confirmVerified(publisher)
    }

    @Test
    fun `tracing list indexes flux should emit items`() {
        collection
            .listIndexes()
            .test()
            .expectSubscription()
            .expectNextCount(1)
            .verifyComplete()
    }

    @Test
    fun `tracing list indexes flux using dedicated subscriber`() {
        tracingFlux.subscribe(Operators.emptySubscriber())
        verify {
            publisher.subscribe(match { it is ScopePassingSpanSubscriber })
        }
        confirmVerified(publisher)
    }
}
