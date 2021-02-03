package com.mongodb.reactor.client

import com.mongodb.CursorType
import com.mongodb.ExplainVerbosity
import com.mongodb.client.model.Collation
import com.mongodb.reactivestreams.client.FindPublisher
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifyAll
import org.bson.BsonDocument
import org.bson.Document
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Operators
import reactor.kotlin.test.test
import java.util.concurrent.TimeUnit

internal class FindFluxTest {

    private val publisher = mockk<FindPublisher<Document>>(relaxed = true)
    private val flux = spyk(FindFlux(publisher))
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
    fun `find flux should always delegate method calls to the underlying publisher`() {
        flux.first()
        val document = BsonDocument()
        flux.filter(document)
        flux.limit(10)
        flux.skip(10)
        flux.maxTime(10, TimeUnit.SECONDS)
        flux.maxAwaitTime(10, TimeUnit.SECONDS)
        flux.projection(document)
        flux.sort(document)
        flux.noCursorTimeout(true)
        flux.oplogReplay(true)
        flux.partial(true)
        flux.cursorType(CursorType.Tailable)
        val collation = Collation.builder().build()
        flux.collation(collation)
        val comment = "Comment"
        flux.comment(comment)
        flux.hint(document)
        val hint = "Hint"
        flux.hintString(hint)
        flux.max(document)
        flux.min(document)
        flux.returnKey(true)
        flux.showRecordId(true)
        flux.batchSize(10)
        flux.allowDiskUse(true)
        flux.explain()
        flux.explain(ExplainVerbosity.QUERY_PLANNER)
        flux.explain(Document::class.java)
        flux.explain(Document::class.java, ExplainVerbosity.QUERY_PLANNER)
        val subscriber = Operators.emptySubscriber<Document>()
        flux.subscribe(subscriber)
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
            publisher.explain()
            publisher.explain(ExplainVerbosity.QUERY_PLANNER)
            publisher.explain(Document::class.java)
            publisher.explain(Document::class.java, ExplainVerbosity.QUERY_PLANNER)
            publisher.subscribe(subscriber)
        }
        confirmVerified(publisher)
    }

    @Test
    fun `find flux should emit items`() {
        collection
            .find(Document("city", "Paris"))
            .map { it["username"] }
            .test()
            .expectSubscription()
            .expectNext("Jean")
            .expectNext("Pierre")
            .verifyComplete()
    }
}
