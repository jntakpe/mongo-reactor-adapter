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

/**
 * Extension that wraps MongoDB's [AggregatePublisher] of [T] into an [AggregateFlux] wrapper class
 * @receiver MongoDB's reactivestreams driver aggregate operation return class
 * @param T the type of the result
 * @return Reactor wrapper for [AggregatePublisher]
 */
public fun <T> AggregatePublisher<T>.toReactor(): AggregateFlux<T> = intoReactor(::AggregateFlux)

/**
 * Extension that wraps MongoDB's [ChangeStreamPublisher] of [T] into an [ChangeStreamFlux] wrapper class
 * @receiver MongoDB's reactivestreams driver change stream operation return class
 * @param T the type of the result
 * @return Reactor wrapper for [ChangeStreamPublisher]
 */
public fun <T> ChangeStreamPublisher<T>.toReactor(): ChangeStreamFlux<T> = intoReactor(::ChangeStreamFlux)

/**
 * Extension that wraps MongoDB's [DistinctPublisher] of [T] into an [DistinctFlux] wrapper class
 * @receiver MongoDB's reactivestreams driver distinct operation return class
 * @param T the type of the result
 * @return Reactor wrapper for [DistinctPublisher]
 */
public fun <T> DistinctPublisher<T>.toReactor(): DistinctFlux<T> = intoReactor(::DistinctFlux)

/**
 * Extension that wraps MongoDB's [FindPublisher] of [T] into an [FindFlux] wrapper class
 * @receiver MongoDB's reactivestreams driver find operation return class
 * @param T the type of the result
 * @return Reactor wrapper for [FindPublisher]
 */
public fun <T> FindPublisher<T>.toReactor(): FindFlux<T> = intoReactor(::FindFlux)

/**
 * Extension that wraps MongoDB's [ListCollectionsPublisher] of [T] into an [ListCollectionFlux] wrapper class
 * @receiver MongoDB's reactivestreams driver list collections operation return class
 * @param T the type of the result
 * @return Reactor wrapper for [ListCollectionsPublisher]
 */
public fun <T> ListCollectionsPublisher<T>.toReactor(): ListCollectionFlux<T> = intoReactor(::ListCollectionFlux)

/**
 * Extension that wraps MongoDB's [ListDatabasesPublisher] of [T] into an [ListDatabaseFlux] wrapper class
 * @receiver MongoDB's reactivestreams driver list databases operation return class
 * @param T the type of the result
 * @return Reactor wrapper for [ListDatabasesPublisher]
 */
public fun <T> ListDatabasesPublisher<T>.toReactor(): ListDatabaseFlux<T> = intoReactor(::ListDatabaseFlux)

/**
 * Extension that wraps MongoDB's [ListIndexesPublisher] of [T] into an [ListIndexesFlux] wrapper class
 * @receiver MongoDB's reactivestreams driver list indexes operation return class
 * @param T the type of the result
 * @return Reactor wrapper for [ListIndexesPublisher]
 */
public fun <T> ListIndexesPublisher<T>.toReactor(): ListIndexesFlux<T> = intoReactor(::ListIndexesFlux)

/**
 * Extension that wraps MongoDB's [MapReducePublisher] of [T] into an [MapReduceFlux] wrapper class
 * @receiver MongoDB's reactivestreams driver map-reduce operation return class
 * @param T the type of the result
 * @return Reactor wrapper for [MapReducePublisher]
 */
public fun <T> MapReducePublisher<T>.toReactor(): MapReduceFlux<T> = intoReactor(::MapReduceFlux)

/**
 * Extension that wraps MongoDB's [MongoCollection] of [T] into an [ReactorMongoCollection] wrapper class
 * @receiver MongoDB's reactivestreams driver collection class
 * @return Reactor wrapper for [MongoCollection]
 */
public fun <T> MongoCollection<T>.toReactor(): ReactorMongoCollection<T> = intoReactor(::ReactorMongoCollection)

/**
 * Extension that wraps MongoDB's [MongoDatabase] into an [ReactorMongoDatabase] wrapper class
 * @receiver MongoDB's reactivestreams driver database class
 * @return Reactor wrapper for [MongoDatabase]
 */
public fun MongoDatabase.toReactor(): ReactorMongoDatabase = intoReactor(::ReactorMongoDatabase)

/**
 * Extension that wraps MongoDB's [MongoClient] into an [ReactorMongoClient] wrapper class
 * @receiver MongoDB's reactivestreams driver client class
 * @return Reactor wrapper for [MongoClient]
 */
public fun MongoClient.toReactor(): ReactorMongoClient = intoReactor(::ReactorMongoClient)

private inline fun <R, reified T> R.intoReactor(ctor: (R) -> T) : T = this as? T ?: ctor(this)