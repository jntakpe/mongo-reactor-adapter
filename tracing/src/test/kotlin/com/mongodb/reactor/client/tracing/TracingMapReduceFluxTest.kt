package com.mongodb.reactor.client.tracing

import brave.Tracing
import com.mongodb.client.model.Collation
import com.mongodb.client.model.MapReduceAction
import com.mongodb.reactivestreams.client.MapReducePublisher
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

internal class TracingMapReduceFluxTest {

    private val tracing = mockk<Tracing>(relaxed = true)
    private val publisher = mockk<MapReducePublisher<Document>>(relaxed = true)
    private val tracingFlux = spyk(TracingMapReduceFlux(publisher, tracing))
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
    fun `tracing map reduce should always delegate method calls to the underlying publisher`() {
        tracingFlux.collectionName(MongoContainer.COLLECTION_NAME)
        val finalizeFunction = "finalize"
        tracingFlux.finalizeFunction(finalizeFunction)
        val document = BsonDocument()
        tracingFlux.scope(document)
        tracingFlux.sort(document)
        tracingFlux.filter(document)
        tracingFlux.limit(10)
        tracingFlux.jsMode(true)
        tracingFlux.verbose(true)
        tracingFlux.maxTime(10, TimeUnit.SECONDS)
        tracingFlux.action(MapReduceAction.MERGE)
        tracingFlux.databaseName(MongoContainer.DATABASE_NAME)
        tracingFlux.sharded(true)
        tracingFlux.nonAtomic(true)
        tracingFlux.bypassDocumentValidation(true)
        tracingFlux.toCollection()
        val collation = Collation.builder().build()
        tracingFlux.collation(collation)
        tracingFlux.batchSize(10)
        tracingFlux.first()
        val subscriber = Operators.emptySubscriber<Document>()
        tracingFlux.subscribe(subscriber)
        verifyAll {
            publisher.collectionName(MongoContainer.COLLECTION_NAME)
            publisher.finalizeFunction(finalizeFunction)
            publisher.scope(document)
            publisher.sort(document)
            publisher.filter(document)
            publisher.limit(10)
            publisher.jsMode(true)
            publisher.verbose(true)
            publisher.maxTime(10, TimeUnit.SECONDS)
            publisher.action(MapReduceAction.MERGE)
            publisher.databaseName(MongoContainer.DATABASE_NAME)
            publisher.sharded(true)
            publisher.nonAtomic(true)
            publisher.bypassDocumentValidation(true)
            publisher.toCollection()
            publisher.collation(collation)
            publisher.batchSize(10)
            publisher.first()
            publisher.subscribe(subscriber)
        }
        confirmVerified(publisher)
    }

    @Test
    fun `tracing map reduce flux should emit items`() {
        collection
            .mapReduce(
                "function map() { emit(1, this.age) }",
                "function reduce(key, values) { return Array.sum(values) }"
            )
            .map { it["value"] }
            .test()
            .expectSubscription()
            .expectNext(110.0)
            .verifyComplete()
    }

    @Test
    fun `tracing map reduce flux using dedicated subscriber`() {
        tracingFlux.subscribe(Operators.emptySubscriber())
        verify {
            publisher.subscribe(match { it is ScopePassingSpanSubscriber })
        }
        confirmVerified(publisher)
    }
}
