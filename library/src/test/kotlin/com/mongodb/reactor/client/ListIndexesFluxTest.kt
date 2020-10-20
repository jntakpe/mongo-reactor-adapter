package com.mongodb.reactor.client

import com.mongodb.reactivestreams.client.ListIndexesPublisher
import com.mongodb.reactor.client.MongoContainer.COLLECTION_NAME
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifyAll
import org.bson.Document
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.DirectProcessor
import reactor.kotlin.test.test
import java.util.concurrent.TimeUnit

internal class ListIndexesFluxTest {

    private val publisher = mockk<ListIndexesPublisher<Document>>(relaxed = true)
    private val flux = spyk(ListIndexesFlux(publisher))
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
    fun `list indexes should always delegate method calls to the underlying publisher`() {
        flux.maxTime(10, TimeUnit.SECONDS)
        flux.batchSize(10)
        flux.first()
        val processor = DirectProcessor.create<Document>()
        flux.subscribe(processor)
        verifyAll {
            publisher.maxTime(10, TimeUnit.SECONDS)
            publisher.batchSize(10)
            publisher.first()
            publisher.subscribe(processor)
        }
        confirmVerified(publisher)
    }

    @Test
    fun `list indexes flux should emit items`() {
        collection
            .listIndexes()
            .test()
            .expectSubscription()
            .expectNextCount(1)
            .verifyComplete()
    }
}
