package com.mongodb.tracingReactor.client.tracing

import brave.Tracing
import com.mongodb.ClientSessionOptions
import com.mongodb.reactivestreams.client.ClientSession
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactor.client.tracing.MongoContainer
import com.mongodb.reactor.client.tracing.TracingReactorMongoClient
import com.mongodb.reactor.client.tracing.toTracingReactor
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifyAll
import org.bson.BsonDocument
import org.bson.Document
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.kotlin.test.test

internal class TracingReactorMongoClientTest {

    private val tracing = mockk<Tracing>(relaxed = true)
    private val original = mockk<MongoClient>(relaxed = true)
    private val tracingReactor = spyk(TracingReactorMongoClient(original, tracing))
    private val client = MongoContainer.client

    @BeforeEach
    fun init() {
        MongoContainer.initData()
    }

    @AfterEach
    fun clean() {
        MongoContainer.clearData()
    }

    @Test
    fun `tracing reactor client should always delegate method calls to the underlying client`() {
        tracingReactor.getDatabase(MongoContainer.DATABASE_NAME)
        tracingReactor.listDatabaseNames()
        val session = mockk<ClientSession>(relaxed = true)
        tracingReactor.listDatabaseNames(session)
        tracingReactor.listDatabases()
        tracingReactor.listDatabases(Document::class.java)
        tracingReactor.listDatabases(session)
        tracingReactor.listDatabases(session, Document::class.java)
        tracingReactor.watch()
        tracingReactor.watch(Document::class.java)
        val documents = listOf(BsonDocument())
        tracingReactor.watch(documents, Document::class.java)
        tracingReactor.watch(session)
        tracingReactor.watch(session, Document::class.java)
        tracingReactor.watch(session, documents)
        tracingReactor.watch(session, documents, Document::class.java)
        tracingReactor.watch(documents)
        tracingReactor.startSession()
        val sessionOptions = ClientSessionOptions.builder().build()
        tracingReactor.startSession(sessionOptions)
        tracingReactor.clusterDescription
        verifyAll {
            original.getDatabase(MongoContainer.DATABASE_NAME)
            original.listDatabaseNames()
            original.listDatabaseNames(session)
            original.listDatabases()
            original.listDatabases(Document::class.java)
            original.listDatabases(session)
            original.listDatabases(session, Document::class.java)
            original.watch()
            original.watch(Document::class.java)
            original.watch(documents, Document::class.java)
            original.watch(session)
            original.watch(session, Document::class.java)
            original.watch(session, documents)
            original.watch(session, documents, Document::class.java)
            original.watch(documents)
            original.startSession()
            original.startSession(sessionOptions)
            original.clusterDescription
        }
        confirmVerified(original)
    }

    @Test
    fun `tracing reactor client should emit items`() {
        client
            .toTracingReactor(tracing)
            .listDatabaseNames()
            .filter { it == MongoContainer.DATABASE_NAME }
            .test()
            .expectSubscription()
            .expectNextCount(1)
            .verifyComplete()
    }
}
