package com.mongodb.reactor.client

import com.mongodb.reactivestreams.client.ListDatabasesPublisher
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

internal class ListDatabaseFluxTest {

    private val publisher = mockk<ListDatabasesPublisher<Document>>(relaxed = true)
    private val flux = spyk(ListDatabaseFlux(publisher))
    private val client = MongoContainer.client.toReactor()

    @BeforeEach
    fun init() {
        MongoContainer.initData()
    }

    @AfterEach
    fun clean() {
        MongoContainer.clearData()
    }

    @Test
    fun `list database flux should always delegate method calls to the underlying publisher`() {
        val document = BsonDocument()
        flux.filter(document)
        flux.maxTime(10, TimeUnit.SECONDS)
        flux.nameOnly(true)
        flux.authorizedDatabasesOnly(true)
        flux.batchSize(10)
        flux.first()
        val subscriber = Operators.emptySubscriber<Document>()
        flux.subscribe(subscriber)
        verifyAll {
            publisher.filter(document)
            publisher.maxTime(10, TimeUnit.SECONDS)
            publisher.nameOnly(true)
            publisher.authorizedDatabasesOnly(true)
            publisher.batchSize(10)
            publisher.first()
            publisher.subscribe(subscriber)
        }
        confirmVerified(publisher)
    }

    @Test
    fun `list database flux should emit items`() {
        client
            .listDatabaseNames()
            .filter { it == MongoContainer.DATABASE_NAME }
            .test()
            .expectSubscription()
            .expectNextCount(1)
            .verifyComplete()
    }
}
