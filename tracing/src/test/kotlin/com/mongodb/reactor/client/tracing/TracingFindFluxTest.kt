package com.mongodb.reactor.client.tracing

import brave.Tracing
import com.mongodb.CursorType
import com.mongodb.client.model.Collation
import com.mongodb.reactivestreams.client.FindPublisher
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

internal class TracingFindFluxTest {

    private val tracing = mockk<Tracing>(relaxed = true)
    private val publisher = mockk<FindPublisher<Document>>(relaxed = true)
    private val tracingFlux = spyk(TracingFindFlux(publisher, tracing))
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
    fun `tracing find flux should always delegate method calls to the underlying publisher`() {
        tracingFlux.first()
        val document = BsonDocument()
        tracingFlux.filter(document)
        tracingFlux.limit(10)
        tracingFlux.skip(10)
        tracingFlux.maxTime(10, TimeUnit.SECONDS)
        tracingFlux.maxAwaitTime(10, TimeUnit.SECONDS)
        tracingFlux.projection(document)
        tracingFlux.sort(document)
        tracingFlux.noCursorTimeout(true)
        tracingFlux.oplogReplay(true)
        tracingFlux.partial(true)
        tracingFlux.cursorType(CursorType.Tailable)
        val collation = Collation.builder().build()
        tracingFlux.collation(collation)
        val comment = "Comment"
        tracingFlux.comment(comment)
        tracingFlux.hint(document)
        val hint = "Hint"
        tracingFlux.hintString(hint)
        tracingFlux.max(document)
        tracingFlux.min(document)
        tracingFlux.returnKey(true)
        tracingFlux.showRecordId(true)
        tracingFlux.batchSize(10)
        tracingFlux.allowDiskUse(true)
        verifyAll {
            publisher.first()
            publisher.filter(document)
            publisher.limit(10)
            publisher.skip(10)
            publisher.maxTime(10, TimeUnit.SECONDS)
            publisher.maxAwaitTime(10, TimeUnit.SECONDS)
            publisher.projection(document)
            publisher.sort(document)
            publisher.noCursorTimeout(true)
            publisher.oplogReplay(true)
            publisher.partial(true)
            publisher.cursorType(CursorType.Tailable)
            publisher.collation(collation)
            publisher.comment(comment)
            publisher.hint(document)
            publisher.hintString(hint)
            publisher.max(document)
            publisher.min(document)
            publisher.returnKey(true)
            publisher.showRecordId(true)
            publisher.batchSize(10)
            publisher.allowDiskUse(true)
        }
        confirmVerified(publisher)
    }

    @Test
    fun `tracing find flux should emit items`() {
        collection
            .toTracingReactor(tracing)
            .find(Document("city", "Paris"))
            .mapNotNull { it["username"] }
            .test()
            .expectSubscription()
            .expectNext("Jean")
            .expectNext("Pierre")
            .verifyComplete()
    }

    @Test
    fun `tracing find flux using dedicated subscriber`() {
        tracingFlux.subscribe(Operators.emptySubscriber())
        verify {
            publisher.subscribe(match { it is ScopePassingSpanSubscriber })
        }
        confirmVerified(publisher)
    }
}
