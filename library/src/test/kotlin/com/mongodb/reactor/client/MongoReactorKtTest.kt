package com.mongodb.reactor.client

import com.mongodb.reactivestreams.client.AggregatePublisher
import com.mongodb.reactivestreams.client.ChangeStreamPublisher
import com.mongodb.reactivestreams.client.DistinctPublisher
import com.mongodb.reactivestreams.client.FindPublisher
import com.mongodb.reactivestreams.client.ListCollectionsPublisher
import com.mongodb.reactivestreams.client.ListDatabasesPublisher
import com.mongodb.reactivestreams.client.ListIndexesPublisher
import com.mongodb.reactivestreams.client.MapReducePublisher
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoCollection
import com.mongodb.reactivestreams.client.MongoDatabase
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MongoReactorKtTest {

    @Test
    fun `to reactor should wrap aggregate publisher into flux`() {
        val delegate = mockk<AggregatePublisher<*>>(relaxed = true)
        val flux = delegate.toReactor()
        assertThat(flux).isNotSameAs(delegate)
        flux.subscribe()
        verify { delegate.subscribe(any()) }
        confirmVerified(delegate)
    }

    @Test
    fun `to reactor should return original aggregate flux`() {
        val delegate = mockk<AggregateFlux<*>>(relaxed = true)
        val flux = delegate.toReactor()
        assertThat(flux).isSameAs(delegate)
    }

    @Test
    fun `to reactor should wrap change stream publisher into flux`() {
        val delegate = mockk<ChangeStreamPublisher<*>>(relaxed = true)
        val flux = delegate.toReactor()
        assertThat(flux).isNotSameAs(delegate)
        flux.subscribe()
        verify { delegate.subscribe(any()) }
        confirmVerified(delegate)
    }

    @Test
    fun `to reactor should return original change stream flux`() {
        val delegate = mockk<ChangeStreamFlux<*>>(relaxed = true)
        val flux = delegate.toReactor()
        assertThat(flux).isSameAs(delegate)
    }

    @Test
    fun `to reactor should wrap distinct publisher into flux`() {
        val delegate = mockk<DistinctPublisher<*>>(relaxed = true)
        val flux = delegate.toReactor()
        assertThat(flux).isNotSameAs(delegate)
        flux.subscribe()
        verify { delegate.subscribe(any()) }
        confirmVerified(delegate)
    }

    @Test
    fun `to reactor should return original distinct flux`() {
        val delegate = mockk<DistinctFlux<*>>(relaxed = true)
        val flux = delegate.toReactor()
        assertThat(flux).isSameAs(delegate)
    }

    @Test
    fun `to reactor should wrap find publisher into flux`() {
        val delegate = mockk<FindPublisher<*>>(relaxed = true)
        val flux = delegate.toReactor()
        assertThat(flux).isNotSameAs(delegate)
        flux.subscribe()
        verify { delegate.subscribe(any()) }
        confirmVerified(delegate)
    }

    @Test
    fun `to reactor should return original find flux`() {
        val delegate = mockk<FindFlux<*>>(relaxed = true)
        val flux = delegate.toReactor()
        assertThat(flux).isSameAs(delegate)
    }

    @Test
    fun `to reactor should wrap list collections publisher into flux`() {
        val delegate = mockk<ListCollectionsPublisher<*>>(relaxed = true)
        val flux = delegate.toReactor()
        assertThat(flux).isNotSameAs(delegate)
        flux.subscribe()
        verify { delegate.subscribe(any()) }
        confirmVerified(delegate)
    }

    @Test
    fun `to reactor should return original list collections flux`() {
        val delegate = mockk<ListCollectionFlux<*>>(relaxed = true)
        val flux = delegate.toReactor()
        assertThat(flux).isSameAs(delegate)
    }

    @Test
    fun `to reactor should wrap list databases publisher into flux`() {
        val delegate = mockk<ListDatabasesPublisher<*>>(relaxed = true)
        val flux = delegate.toReactor()
        assertThat(flux).isNotSameAs(delegate)
        flux.subscribe()
        verify { delegate.subscribe(any()) }
        confirmVerified(delegate)
    }

    @Test
    fun `to reactor should return original list databases flux`() {
        val delegate = mockk<ListDatabaseFlux<*>>(relaxed = true)
        val flux = delegate.toReactor()
        assertThat(flux).isSameAs(delegate)
    }

    @Test
    fun `to reactor should wrap list indexes publisher into flux`() {
        val delegate = mockk<ListIndexesPublisher<*>>(relaxed = true)
        val flux = delegate.toReactor()
        assertThat(flux).isNotSameAs(delegate)
        flux.subscribe()
        verify { delegate.subscribe(any()) }
        confirmVerified(delegate)
    }

    @Test
    fun `to reactor should return original list indexes flux`() {
        val delegate = mockk<ListIndexesFlux<*>>(relaxed = true)
        val flux = delegate.toReactor()
        assertThat(flux).isSameAs(delegate)
    }

    @Test
    fun `to reactor should wrap map reduce publisher into flux`() {
        val delegate = mockk<MapReducePublisher<*>>(relaxed = true)
        val flux = delegate.toReactor()
        assertThat(flux).isNotSameAs(delegate)
        flux.subscribe()
        verify { delegate.subscribe(any()) }
        confirmVerified(delegate)
    }

    @Test
    fun `to reactor should return original map reduce flux`() {
        val delegate = mockk<MapReduceFlux<*>>(relaxed = true)
        val flux = delegate.toReactor()
        assertThat(flux).isSameAs(delegate)
    }

    @Test
    fun `to reactor should wrap collection into reactor collection`() {
        val delegate = mockk<MongoCollection<*>>(relaxed = true)
        val flux = delegate.toReactor()
        assertThat(flux).isNotSameAs(delegate)
        flux.listIndexes()
        verify { delegate.listIndexes() }
        confirmVerified(delegate)
    }

    @Test
    fun `to reactor should return original reactor collection`() {
        val delegate = mockk<ReactorMongoCollection<*>>(relaxed = true)
        val flux = delegate.toReactor()
        assertThat(flux).isSameAs(delegate)
    }

    @Test
    fun `to reactor should wrap mongo database into reactor database`() {
        val delegate = mockk<MongoDatabase>(relaxed = true)
        val reactor = delegate.toReactor()
        assertThat(reactor).isNotSameAs(delegate)
        reactor.listCollectionNames()
        verify { delegate.listCollectionNames() }
        confirmVerified(delegate)
    }

    @Test
    fun `to reactor should return original reactor database`() {
        val delegate = mockk<ReactorMongoDatabase>(relaxed = true)
        val flux = delegate.toReactor()
        assertThat(flux).isSameAs(delegate)
    }

    @Test
    fun `to reactor should wrap mongo client into reactor client`() {
        val delegate = mockk<MongoClient>(relaxed = true)
        val reactor = delegate.toReactor()
        assertThat(reactor).isNotSameAs(delegate)
        reactor.listDatabaseNames()
        verify { delegate.listDatabaseNames() }
        confirmVerified(delegate)
    }

    @Test
    fun `to reactor should return original reactor client`() {
        val delegate = mockk<ReactorMongoClient>(relaxed = true)
        val flux = delegate.toReactor()
        assertThat(flux).isSameAs(delegate)
    }
}
