package com.mongodb.reactor.client

import com.mongodb.ClientSessionOptions
import com.mongodb.reactivestreams.client.ClientSession
import com.mongodb.reactivestreams.client.MongoClient
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
import reactor.kotlin.test.test

internal class ReactorMongoClientTest {

    private val original = mockk<MongoClient>(relaxed = true)
    private val reactor = spyk(ReactorMongoClient(original))
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
    fun `reactor client should always delegate method calls to the underlying client`() {
        reactor.getDatabase(DATABASE_NAME)
        reactor.listDatabaseNames()
        val session = mockk<ClientSession>(relaxed = true)
        reactor.listDatabaseNames(session)
        reactor.listDatabases()
        reactor.listDatabases(Document::class.java)
        reactor.listDatabases(session)
        reactor.listDatabases(session, Document::class.java)
        reactor.watch()
        reactor.watch(Document::class.java)
        val documents = listOf(BsonDocument())
        reactor.watch(documents, Document::class.java)
        reactor.watch(session)
        reactor.watch(session, Document::class.java)
        reactor.watch(session, documents)
        reactor.watch(session, documents, Document::class.java)
        reactor.watch(documents)
        reactor.startSession()
        val sessionOptions = ClientSessionOptions.builder().build()
        reactor.startSession(sessionOptions)
        reactor.clusterDescription
        verifyAll {
            original.getDatabase(DATABASE_NAME)
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
    fun `reactor client should emit items`() {
        client
            .listDatabaseNames()
            .filter { it == DATABASE_NAME }
            .test()
            .expectSubscription()
            .expectNextCount(1)
            .verifyComplete()
    }
}
