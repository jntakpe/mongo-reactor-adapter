package com.mongodb.reactor.client

import com.mongodb.reactivestreams.client.ListCollectionsPublisher
import com.mongodb.reactor.client.MongoContainer.COLLECTION_NAME
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

internal class ListCollectionFluxTest {

    private val publisher = mockk<ListCollectionsPublisher<Document>>(relaxed = true)
    private val flux = spyk(ListCollectionFlux(publisher))
    private val database = MongoContainer.database.toReactor()

    @BeforeEach
    fun init() {
        MongoContainer.initData()
    }

    @AfterEach
    fun clean() {
        MongoContainer.clearData()
    }

    @Test
    fun `list collection flux should always delegate method calls to the underlying publisher`() {
        val document = BsonDocument()
        flux.filter(document)
        flux.maxTime(10, TimeUnit.SECONDS)
        flux.batchSize(10)
        flux.first()
        val subscriber = Operators.emptySubscriber<Document>()
        flux.subscribe(subscriber)
        verifyAll {
            publisher.filter(document)
            publisher.maxTime(10, TimeUnit.SECONDS)
            publisher.batchSize(10)
            publisher.first()
            publisher.subscribe(subscriber)
        }
        confirmVerified(publisher)
    }

    @Test
    fun `list collection flux should emit items`() {
        database
            .listCollections()
            .mapNotNull { it["name"] }
            .test()
            .expectSubscription()
            .expectNext(COLLECTION_NAME)
            .verifyComplete()
    }
}
