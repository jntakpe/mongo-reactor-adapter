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

public fun <T> AggregatePublisher<T>.toReactor(): AggregateFlux<T> = AggregateFlux(this)

public fun <T> ChangeStreamPublisher<T>.toReactor(): ChangeStreamFlux<T> = ChangeStreamFlux(this)

public fun <T> DistinctPublisher<T>.toReactor(): DistinctFlux<T> = DistinctFlux(this)

public fun <T> FindPublisher<T>.toReactor(): FindFlux<T> = FindFlux(this)

public fun <T> ListCollectionsPublisher<T>.toReactor(): ListCollectionFlux<T> = ListCollectionFlux(this)

public fun <T> ListDatabasesPublisher<T>.toReactor(): ListDatabaseFlux<T> = ListDatabaseFlux(this)

public fun <T> ListIndexesPublisher<T>.toReactor(): ListIndexesFlux<T> = ListIndexesFlux(this)

public fun <T> MapReducePublisher<T>.toReactor(): MapReduceFlux<T> = MapReduceFlux(this)

public fun <T> MongoCollection<T>.toReactor(): ReactorMongoCollection<T> = ReactorMongoCollection(this)

public fun MongoDatabase.toReactor(): ReactorMongoDatabase = ReactorMongoDatabase(this)

public fun MongoClient.toReactor(): ReactorMongoClient = ReactorMongoClient(this)
