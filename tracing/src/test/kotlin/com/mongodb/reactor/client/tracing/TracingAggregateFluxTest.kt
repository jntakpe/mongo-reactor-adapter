package com.mongodb.reactor.client.tracing

import brave.Tracing
import com.mongodb.BasicDBObject
import com.mongodb.client.model.Aggregates.match
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Collation
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import com.mongodb.reactivestreams.client.AggregatePublisher
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

internal class TracingAggregateFluxTest {

    private val tracing = mockk<Tracing>(relaxed = true)
    private val publisher = mockk<AggregatePublisher<Document>>(relaxed = true)
    private val tracingFlux = spyk(TracingAggregateFlux(publisher, tracing))
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
    fun `tracing aggregate flux should always delegate method calls to the underlying aggregate flux`() {
        tracingFlux.allowDiskUse(true)
        tracingFlux.maxTime(10, TimeUnit.SECONDS)
        tracingFlux.maxAwaitTime(100, TimeUnit.SECONDS)
        tracingFlux.bypassDocumentValidation(true)
        tracingFlux.toCollection()
        val collation = Collation.builder().build()
        tracingFlux.collation(collation)
        tracingFlux.comment("test")
        val hint = BasicDBObject("test", "value")
        tracingFlux.hint(hint)
        tracingFlux.hintString(hint.toString())
        tracingFlux.batchSize(10)
        tracingFlux.let(hint)
        tracingFlux.first()
        verifyAll {
            publisher.allowDiskUse(true)
            publisher.maxTime(10, TimeUnit.SECONDS)
            publisher.maxAwaitTime(100, TimeUnit.SECONDS)
            publisher.bypassDocumentValidation(true)
            publisher.toCollection()
            publisher.collation(collation)
            publisher.comment("test")
            publisher.hint(hint)
            publisher.hintString(hint.toString())
            publisher.batchSize(10)
            publisher.let(hint)
            publisher.first()
        }
        confirmVerified(publisher)
    }

    @Test
    fun `tracing aggregate flux should emit items`() {
        collection
            .toTracingReactor(tracing)
            .aggregate(listOf(match(Filters.eq("city", "Paris")), sort(Sorts.descending("username"))))
            .mapNotNull { it["username"] }
            .test()
            .expectSubscription()
            .expectNext("Pierre")
            .expectNext("Jean")
            .verifyComplete()
    }

    @Test
    fun `tracing aggregate flux should subscribe using dedicated subscriber`() {
        tracingFlux.subscribe(Operators.emptySubscriber())
        verify {
            publisher.subscribe(match { it is ScopePassingSpanSubscriber })
        }
        confirmVerified(publisher)
    }
}
