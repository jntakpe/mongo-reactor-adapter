package com.mongodb.reactor.client

import com.mongodb.client.model.Collation
import com.mongodb.client.model.MapReduceAction
import com.mongodb.reactivestreams.client.MapReducePublisher
import com.mongodb.reactor.client.MongoContainer.COLLECTION_NAME
import com.mongodb.reactor.client.MongoContainer.DATABASE_NAME
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

internal class MapReduceFluxTest {

    private val publisher = mockk<MapReducePublisher<Document>>(relaxed = true)
    private val flux = spyk(MapReduceFlux(publisher))
    private val collection = MongoContainer.database.getCollection(COLLECTION_NAME).toReactor()

    @BeforeEach
    fun init() {
        MongoContainer.initData()
    }

    @AfterEach
    fun clean() {
        MongoContainer.clearData()
    }

    @Test
    fun `map reduce should always delegate method calls to the underlying publisher`() {
        flux.collectionName(COLLECTION_NAME)
        val finalizeFunction = "finalize"
        flux.finalizeFunction(finalizeFunction)
        val document = BsonDocument()
        flux.scope(document)
        flux.sort(document)
        flux.filter(document)
        flux.limit(10)
        flux.jsMode(true)
        flux.verbose(true)
        flux.maxTime(10, TimeUnit.SECONDS)
        flux.action(MapReduceAction.MERGE)
        flux.databaseName(DATABASE_NAME)
        flux.sharded(true)
        flux.nonAtomic(true)
        flux.bypassDocumentValidation(true)
        flux.toCollection()
        val collation = Collation.builder().build()
        flux.collation(collation)
        flux.batchSize(10)
        flux.first()
        val subscriber = Operators.emptySubscriber<Document>()
        flux.subscribe(subscriber)
        verifyAll {
            publisher.collectionName(COLLECTION_NAME)
            publisher.finalizeFunction(finalizeFunction)
            publisher.scope(document)
            publisher.sort(document)
            publisher.filter(document)
            publisher.limit(10)
            publisher.jsMode(true)
            publisher.verbose(true)
            publisher.maxTime(10, TimeUnit.SECONDS)
            publisher.action(MapReduceAction.MERGE)
            publisher.databaseName(DATABASE_NAME)
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
    fun `map reduce flux should emit items`() {
        collection
            .mapReduce(
                "function map() { emit(1, this.age) }",
                "function reduce(key, values) { return Array.sum(values) }"
            )
            .mapNotNull { it["value"] }
            .test()
            .expectSubscription()
            .expectNext(110.0)
            .verifyComplete()
    }
}
