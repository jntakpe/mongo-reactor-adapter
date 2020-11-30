package com.mongodb.reactor.client

import com.mongodb.client.model.Collation
import com.mongodb.reactivestreams.client.DistinctPublisher
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

internal class DistinctFluxTest {

    private val publisher = mockk<DistinctPublisher<Document>>(relaxed = true)
    private val flux = spyk(DistinctFlux(publisher))
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
    fun `distinct flux should always delegate method calls to the underlying publisher`() {
        val document = BsonDocument()
        flux.filter(document)
        flux.maxTime(10L, TimeUnit.SECONDS)
        val collation = Collation.builder().build()
        flux.collation(collation)
        flux.batchSize(10)
        flux.first()
        val subscriber = Operators.emptySubscriber<Document>()
        flux.subscribe(subscriber)
        verifyAll {
            publisher.filter(document)
            publisher.maxTime(10L, TimeUnit.SECONDS)
            publisher.collation(collation)
            publisher.batchSize(10)
            publisher.first()
            publisher.subscribe(subscriber)
        }
        confirmVerified(publisher)
    }

    @Test
    fun `distinct flux should emit items`() {
        collection
            .distinct("city", String::class.java)
            .test()
            .expectSubscription()
            .expectNext("London")
            .expectNext("Paris")
            .verifyComplete()
    }
}
