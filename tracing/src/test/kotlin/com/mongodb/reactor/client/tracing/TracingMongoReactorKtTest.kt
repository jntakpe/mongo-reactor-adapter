package com.mongodb.reactor.client.tracing

import brave.Tracing
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

internal class TracingMongoReactorKtTest {

    private val tracing = mockk<Tracing>(relaxed = true)

    @Test
    fun `to tracing reactor should wrap aggregate publisher into flux`() {
        val delegate = mockk<AggregatePublisher<*>>(relaxed = true)
        val flux = delegate.toTracingReactor(tracing)
        assertThat(flux).isNotSameAs(delegate)
        flux.subscribe()
        verify { delegate.subscribe(any()) }
        confirmVerified(delegate)
    }

    @Test
    fun `to tracing reactor should return original aggregate flux`() {
        val delegate = mockk<TracingAggregateFlux<*>>(relaxed = true)
        val flux = delegate.toTracingReactor(tracing)
        assertThat(flux).isSameAs(delegate)
    }

    @Test
    fun `to tracing reactor should wrap change stream publisher into flux`() {
        val delegate = mockk<ChangeStreamPublisher<*>>(relaxed = true)
        val flux = delegate.toTracingReactor(tracing)
        assertThat(flux).isNotSameAs(delegate)
        flux.subscribe()
        verify { delegate.subscribe(any()) }
        confirmVerified(delegate)
    }

    @Test
    fun `to tracing reactor should return original change stream flux`() {
        val delegate = mockk<TracingChangeStreamFlux<*>>(relaxed = true)
        val flux = delegate.toTracingReactor(tracing)
        assertThat(flux).isSameAs(delegate)
    }

    @Test
    fun `to tracing reactor should wrap distinct publisher into flux`() {
        val delegate = mockk<DistinctPublisher<*>>(relaxed = true)
        val flux = delegate.toTracingReactor(tracing)
        assertThat(flux).isNotSameAs(delegate)
        flux.subscribe()
        verify { delegate.subscribe(any()) }
        confirmVerified(delegate)
    }

    @Test
    fun `to tracing reactor should return original distinct flux`() {
        val delegate = mockk<TracingDistinctFlux<*>>(relaxed = true)
        val flux = delegate.toTracingReactor(tracing)
        assertThat(flux).isSameAs(delegate)
    }

    @Test
    fun `to tracing reactor should wrap find publisher into flux`() {
        val delegate = mockk<FindPublisher<*>>(relaxed = true)
        val flux = delegate.toTracingReactor(tracing)
        assertThat(flux).isNotSameAs(delegate)
        flux.subscribe()
        verify { delegate.subscribe(any()) }
        confirmVerified(delegate)
    }

    @Test
    fun `to tracing reactor should return original find flux`() {
        val delegate = mockk<TracingFindFlux<*>>(relaxed = true)
        val flux = delegate.toTracingReactor(tracing)
        assertThat(flux).isSameAs(delegate)
    }

    @Test
    fun `to tracing reactor should wrap list collections publisher into flux`() {
        val delegate = mockk<ListCollectionsPublisher<*>>(relaxed = true)
        val flux = delegate.toTracingReactor(tracing)
        assertThat(flux).isNotSameAs(delegate)
        flux.subscribe()
        verify { delegate.subscribe(any()) }
        confirmVerified(delegate)
    }

    @Test
    fun `to tracing reactor should return original list collections flux`() {
        val delegate = mockk<TracingListCollectionFlux<*>>(relaxed = true)
        val flux = delegate.toTracingReactor(tracing)
        assertThat(flux).isSameAs(delegate)
    }

    @Test
    fun `to tracing reactor should wrap list databases publisher into flux`() {
        val delegate = mockk<ListDatabasesPublisher<*>>(relaxed = true)
        val flux = delegate.toTracingReactor(tracing)
        assertThat(flux).isNotSameAs(delegate)
        flux.subscribe()
        verify { delegate.subscribe(any()) }
        confirmVerified(delegate)
    }

    @Test
    fun `to tracing reactor should return original list databases flux`() {
        val delegate = mockk<TracingListDatabaseFlux<*>>(relaxed = true)
        val flux = delegate.toTracingReactor(tracing)
        assertThat(flux).isSameAs(delegate)
    }

    @Test
    fun `to tracing reactor should wrap list indexes publisher into flux`() {
        val delegate = mockk<ListIndexesPublisher<*>>(relaxed = true)
        val flux = delegate.toTracingReactor(tracing)
        assertThat(flux).isNotSameAs(delegate)
        flux.subscribe()
        verify { delegate.subscribe(any()) }
        confirmVerified(delegate)
    }

    @Test
    fun `to tracing reactor should return original list indexes flux`() {
        val delegate = mockk<TracingListIndexesFlux<*>>(relaxed = true)
        val flux = delegate.toTracingReactor(tracing)
        assertThat(flux).isSameAs(delegate)
    }

    @Test
    fun `to tracing reactor should wrap map reduce publisher into flux`() {
        val delegate = mockk<MapReducePublisher<*>>(relaxed = true)
        val flux = delegate.toTracingReactor(tracing)
        assertThat(flux).isNotSameAs(delegate)
        flux.subscribe()
        verify { delegate.subscribe(any()) }
        confirmVerified(delegate)
    }

    @Test
    fun `to tracing reactor should return original map reduce flux`() {
        val delegate = mockk<TracingMapReduceFlux<*>>(relaxed = true)
        val flux = delegate.toTracingReactor(tracing)
        assertThat(flux).isSameAs(delegate)
    }

    @Test
    fun `to tracing reactor should wrap collection into reactor collection`() {
        val delegate = mockk<MongoCollection<*>>(relaxed = true)
        val flux = delegate.toTracingReactor(tracing)
        assertThat(flux).isNotSameAs(delegate)
        flux.listIndexes()
        verify { delegate.listIndexes() }
        confirmVerified(delegate)
    }

    @Test
    fun `to tracing reactor should return original reactor collection`() {
        val delegate = mockk<TracingReactorMongoCollection<*>>(relaxed = true)
        val flux = delegate.toTracingReactor(tracing)
        assertThat(flux).isSameAs(delegate)
    }

    @Test
    fun `to tracing reactor should wrap mongo database into reactor database`() {
        val delegate = mockk<MongoDatabase>(relaxed = true)
        val reactor = delegate.toTracingReactor(tracing)
        assertThat(reactor).isNotSameAs(delegate)
        reactor.listCollectionNames()
        verify { delegate.listCollectionNames() }
        confirmVerified(delegate)
    }

    @Test
    fun `to tracing reactor should return original reactor database`() {
        val delegate = mockk<TracingReactorMongoDatabase>(relaxed = true)
        val flux = delegate.toTracingReactor(tracing)
        assertThat(flux).isSameAs(delegate)
    }

    @Test
    fun `to tracing reactor should wrap mongo client into reactor client`() {
        val delegate = mockk<MongoClient>(relaxed = true)
        val reactor = delegate.toTracingReactor(tracing)
        assertThat(reactor).isNotSameAs(delegate)
        reactor.listDatabaseNames()
        verify { delegate.listDatabaseNames() }
        confirmVerified(delegate)
    }

    @Test
    fun `to tracing reactor should return original reactor client`() {
        val delegate = mockk<TracingReactorMongoClient>(relaxed = true)
        val flux = delegate.toTracingReactor(tracing)
        assertThat(flux).isSameAs(delegate)
    }
}
