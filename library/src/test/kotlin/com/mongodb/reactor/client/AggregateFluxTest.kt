package com.mongodb.reactor.client

import com.mongodb.BasicDBObject
import com.mongodb.ExplainVerbosity
import com.mongodb.client.model.Aggregates.match
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Collation
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import com.mongodb.reactivestreams.client.AggregatePublisher
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifyAll
import org.bson.Document
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Operators
import reactor.kotlin.test.test
import java.util.concurrent.TimeUnit

internal class AggregateFluxTest {

    private val publisher = mockk<AggregatePublisher<Document>>(relaxed = true)
    private val flux = spyk(AggregateFlux(publisher))
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
    fun `aggregate flux should always delegate method calls to the underlying publisher`() {
        flux.allowDiskUse(true)
        flux.maxTime(10, TimeUnit.SECONDS)
        flux.maxAwaitTime(100, TimeUnit.SECONDS)
        flux.bypassDocumentValidation(true)
        flux.toCollection()
        val collation = Collation.builder().build()
        flux.collation(collation)
        flux.comment("test")
        val hint = BasicDBObject("test", "value")
        flux.hint(hint)
        flux.batchSize(10)
        flux.first()
        flux.explain()
        flux.explain(ExplainVerbosity.QUERY_PLANNER)
        flux.explain(Document::class.java)
        flux.explain(Document::class.java, ExplainVerbosity.QUERY_PLANNER)
        val subscriber = Operators.emptySubscriber<Document>()
        flux.subscribe(subscriber)
        verifyAll {
            publisher.allowDiskUse(true)
            publisher.maxTime(10, TimeUnit.SECONDS)
            publisher.maxAwaitTime(100, TimeUnit.SECONDS)
            publisher.bypassDocumentValidation(true)
            publisher.toCollection()
            publisher.collation(collation)
            publisher.comment("test")
            publisher.hint(hint)
            publisher.batchSize(10)
            publisher.first()
            publisher.explain()
            publisher.explain(ExplainVerbosity.QUERY_PLANNER)
            publisher.explain(Document::class.java)
            publisher.explain(Document::class.java, ExplainVerbosity.QUERY_PLANNER)
            publisher.subscribe(subscriber)
        }
        confirmVerified(publisher)
    }

    @Test
    fun `aggregate flux should emit items`() {
        collection
            .aggregate(listOf(match(Filters.eq("city", "Paris")), sort(Sorts.descending("username"))))
            .mapNotNull { it["username"] }
            .test()
            .expectSubscription()
            .expectNext("Pierre")
            .expectNext("Jean")
            .verifyComplete()
    }
}
