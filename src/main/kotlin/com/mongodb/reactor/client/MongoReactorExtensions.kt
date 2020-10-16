package com.mongodb.reactor.client

import com.mongodb.reactivestreams.client.AggregatePublisher
import com.mongodb.reactivestreams.client.ChangeStreamPublisher
import com.mongodb.reactivestreams.client.DistinctPublisher
import com.mongodb.reactivestreams.client.FindPublisher
import com.mongodb.reactivestreams.client.ListCollectionsPublisher
import com.mongodb.reactivestreams.client.ListIndexesPublisher
import com.mongodb.reactivestreams.client.MapReducePublisher

fun <T> AggregatePublisher<T>.toReactor() = AggregateFlux(this)

fun <T> ChangeStreamPublisher<T>.toReactor() = ChangeStreamFlux(this)

fun <T> DistinctPublisher<T>.toReactor() = DistinctFlux(this)

fun <T> FindPublisher<T>.toReactor() = FindFlux(this)

fun <T> ListCollectionsPublisher<T>.toReactor() = ListCollectionFlux(this)

fun <T> ListIndexesPublisher<T>.toReactor() = ListIndexesFlux(this)

fun <T> MapReducePublisher<T>.toReactor() = MapReduceFlux(this)
