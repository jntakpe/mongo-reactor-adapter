package com.mongodb.reactor.client.tracing

import brave.Tracing
import com.mongodb.client.model.Collation
import com.mongodb.reactivestreams.client.DistinctPublisher
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

internal class TracingDistinctFluxTest {

    private val tracing = mockk<Tracing>(relaxed = true)
    private val publisher = mockk<DistinctPublisher<Document>>(relaxed = true)
    private val tracingFlux = spyk(TracingDistinctFlux(publisher, tracing))
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
    fun `tracing distinct flux should always delegate method calls to the underlying publisher`() {
        val document = BsonDocument()
        tracingFlux.filter(document)
        tracingFlux.maxTime(10L, TimeUnit.SECONDS)
        val collation = Collation.builder().build()
        tracingFlux.collation(collation)
        tracingFlux.batchSize(10)
        tracingFlux.first()
        verifyAll {
            publisher.filter(document)
            publisher.maxTime(10L, TimeUnit.SECONDS)
            publisher.collation(collation)
            publisher.batchSize(10)
            publisher.first()
        }
        confirmVerified(publisher)
    }

    @Test
    fun `tracing distinct flux should emit items`() {
        collection
            .toTracingReactor(tracing)
            .distinct("city", String::class.java)
            .test()
            .expectSubscription()
            .expectNext("London")
            .expectNext("Paris")
            .verifyComplete()
    }

    @Test
    fun `tracing distinct flux using dedicated subscriber`() {
        tracingFlux.subscribe(Operators.emptySubscriber())
        verify {
            publisher.subscribe(match { it is ScopePassingSpanSubscriber })
        }
        confirmVerified(publisher)
    }
}
