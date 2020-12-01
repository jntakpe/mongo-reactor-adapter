package com.mongodb.reactor.client.tracing

import brave.Tracing
import com.mongodb.MongoNamespace
import com.mongodb.ReadConcern
import com.mongodb.ReadPreference
import com.mongodb.WriteConcern
import com.mongodb.bulk.BulkWriteResult
import com.mongodb.client.model.BulkWriteOptions
import com.mongodb.client.model.CountOptions
import com.mongodb.client.model.CreateIndexOptions
import com.mongodb.client.model.DeleteOptions
import com.mongodb.client.model.DropIndexOptions
import com.mongodb.client.model.EstimatedDocumentCountOptions
import com.mongodb.client.model.FindOneAndDeleteOptions
import com.mongodb.client.model.FindOneAndReplaceOptions
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.IndexModel
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.InsertManyOptions
import com.mongodb.client.model.InsertOneOptions
import com.mongodb.client.model.RenameCollectionOptions
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.WriteModel
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.InsertManyResult
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult
import com.mongodb.reactivestreams.client.ClientSession
import com.mongodb.reactivestreams.client.MongoCollection
import com.mongodb.reactor.client.ReactorMongoCollection
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistry
import org.bson.conversions.Bson
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Wraps MongoDB's reactivestreams driver [ReactorMongoCollection] class.
 * Delegates every call to the original MongoDB's reactivestreams driver class but transform ReactiveStream's Publisher return types
 * into either [Flux] or [Mono]  subscribing with an OpenTracing aware subscriber that continues spans.
 * @param delegate original MongoDB's reactivestreams driver class to which all the calls are delegated
 * @param tracing OpenTracing's instrumentation
 * @see ReactorMongoCollection
 */
public class TracingReactorMongoCollection<T>(private val delegate: MongoCollection<T>, private val tracing: Tracing) :
    MongoCollection<T> by delegate {

    override fun <NewTDocument : Any> withDocumentClass(clazz: Class<NewTDocument>): TracingReactorMongoCollection<NewTDocument> {
        return TracingReactorMongoCollection(delegate.withDocumentClass(clazz), tracing)
    }

    override fun withCodecRegistry(codecRegistry: CodecRegistry): TracingReactorMongoCollection<T> {
        return TracingReactorMongoCollection(delegate.withCodecRegistry(codecRegistry), tracing)
    }

    override fun withReadPreference(readPreference: ReadPreference): TracingReactorMongoCollection<T> {
        return TracingReactorMongoCollection(delegate.withReadPreference(readPreference), tracing)
    }

    override fun withWriteConcern(writeConcern: WriteConcern): TracingReactorMongoCollection<T> {
        return TracingReactorMongoCollection(delegate.withWriteConcern(writeConcern), tracing)
    }

    override fun withReadConcern(readConcern: ReadConcern): TracingReactorMongoCollection<T> {
        return TracingReactorMongoCollection(delegate.withReadConcern(readConcern), tracing)
    }

    override fun estimatedDocumentCount(): Mono<Long> = delegate.estimatedDocumentCount().toTracingMono(tracing)

    override fun estimatedDocumentCount(options: EstimatedDocumentCountOptions): Mono<Long> {
        return delegate.estimatedDocumentCount(options).toTracingMono(tracing)
    }

    override fun countDocuments(): Mono<Long> = delegate.countDocuments().toTracingMono(tracing)

    override fun countDocuments(filter: Bson): Mono<Long> = delegate.countDocuments(filter).toTracingMono(tracing)

    override fun countDocuments(filter: Bson, options: CountOptions): Mono<Long> {
        return delegate.countDocuments(filter, options).toTracingMono(tracing)
    }

    override fun countDocuments(clientSession: ClientSession): Mono<Long> = delegate.countDocuments(clientSession).toTracingMono(tracing)

    override fun countDocuments(clientSession: ClientSession, filter: Bson): Mono<Long> {
        return delegate.countDocuments(clientSession, filter).toTracingMono(tracing)
    }

    override fun countDocuments(clientSession: ClientSession, filter: Bson, options: CountOptions): Mono<Long> {
        return delegate.countDocuments(clientSession, filter, options).toTracingMono(tracing)
    }

    override fun <TResult : Any> distinct(fieldName: String, resultClass: Class<TResult>): TracingDistinctFlux<TResult> {
        return delegate.distinct(fieldName, resultClass).toTracingReactor(tracing)
    }

    override fun <TResult : Any> distinct(fieldName: String, filter: Bson, resultClass: Class<TResult>): TracingDistinctFlux<TResult> {
        return delegate.distinct(fieldName, filter, resultClass).toTracingReactor(tracing)
    }

    override fun <TResult : Any> distinct(
        clientSession: ClientSession,
        fieldName: String,
        resultClass: Class<TResult>,
    ): TracingDistinctFlux<TResult> {
        return delegate.distinct(clientSession, fieldName, resultClass).toTracingReactor(tracing)
    }

    override fun <TResult : Any> distinct(
        clientSession: ClientSession,
        fieldName: String,
        filter: Bson,
        resultClass: Class<TResult>,
    ): TracingDistinctFlux<TResult> {
        return delegate.distinct(clientSession, fieldName, filter, resultClass).toTracingReactor(tracing)
    }

    override fun find(): TracingFindFlux<T> = delegate.find().toTracingReactor(tracing)

    override fun <TResult : Any> find(clazz: Class<TResult>): TracingFindFlux<TResult> = delegate.find(clazz).toTracingReactor(tracing)

    override fun find(filter: Bson): TracingFindFlux<T> = delegate.find(filter).toTracingReactor(tracing)

    override fun <TResult : Any> find(filter: Bson, clazz: Class<TResult>): TracingFindFlux<TResult> {
        return delegate.find(filter, clazz).toTracingReactor(tracing)
    }

    override fun find(clientSession: ClientSession): TracingFindFlux<T> = delegate.find(clientSession).toTracingReactor(tracing)

    override fun <TResult : Any> find(clientSession: ClientSession, clazz: Class<TResult>): TracingFindFlux<TResult> {
        return delegate.find(clientSession, clazz).toTracingReactor(tracing)
    }

    override fun find(clientSession: ClientSession, filter: Bson): TracingFindFlux<T> {
        return delegate.find(clientSession, filter).toTracingReactor(tracing)
    }

    override fun <TResult : Any> find(clientSession: ClientSession, filter: Bson, clazz: Class<TResult>): TracingFindFlux<TResult> {
        return delegate.find(clientSession, filter, clazz).toTracingReactor(tracing)
    }

    override fun aggregate(pipeline: List<Bson>): TracingAggregateFlux<T> = delegate.aggregate(pipeline).toTracingReactor(tracing)

    override fun <TResult : Any> aggregate(pipeline: List<Bson>, clazz: Class<TResult>): TracingAggregateFlux<TResult> {
        return delegate.aggregate(pipeline, clazz).toTracingReactor(tracing)
    }

    override fun aggregate(clientSession: ClientSession, pipeline: List<Bson>): TracingAggregateFlux<T> {
        return delegate.aggregate(clientSession, pipeline).toTracingReactor(tracing)
    }

    override fun <TResult : Any> aggregate(
        clientSession: ClientSession,
        pipeline: List<Bson>,
        clazz: Class<TResult>,
    ): TracingAggregateFlux<TResult> {
        return delegate.aggregate(clientSession, pipeline, clazz).toTracingReactor(tracing)
    }

    override fun watch(): TracingChangeStreamFlux<Document> = delegate.watch().toTracingReactor(tracing)

    override fun <TResult : Any> watch(resultClass: Class<TResult>): TracingChangeStreamFlux<TResult> =
        delegate.watch(resultClass).toTracingReactor(tracing)

    override fun watch(pipeline: List<Bson>): TracingChangeStreamFlux<Document> = delegate.watch(pipeline).toTracingReactor(tracing)

    override fun <TResult : Any> watch(pipeline: List<Bson>, resultClass: Class<TResult>): TracingChangeStreamFlux<TResult> {
        return delegate.watch(pipeline, resultClass).toTracingReactor(tracing)
    }

    override fun watch(clientSession: ClientSession): TracingChangeStreamFlux<Document> {
        return delegate.watch(clientSession).toTracingReactor(tracing)
    }

    override fun <TResult : Any> watch(clientSession: ClientSession, resultClass: Class<TResult>): TracingChangeStreamFlux<TResult> {
        return delegate.watch(clientSession, resultClass).toTracingReactor(tracing)
    }

    override fun watch(clientSession: ClientSession, pipeline: List<Bson>): TracingChangeStreamFlux<Document> {
        return delegate.watch(clientSession, pipeline).toTracingReactor(tracing)
    }

    override fun <TResult : Any> watch(
        clientSession: ClientSession,
        pipeline: List<Bson>,
        resultClass: Class<TResult>,
    ): TracingChangeStreamFlux<TResult> {
        return delegate.watch(clientSession, pipeline, resultClass).toTracingReactor(tracing)
    }

    override fun mapReduce(mapFunction: String, reduceFunction: String): TracingMapReduceFlux<T> {
        return delegate.mapReduce(mapFunction, reduceFunction).toTracingReactor(tracing)
    }

    override fun <TResult : Any> mapReduce(
        mapFunction: String,
        reduceFunction: String,
        clazz: Class<TResult>,
    ): TracingMapReduceFlux<TResult> {
        return delegate.mapReduce(mapFunction, reduceFunction, clazz).toTracingReactor(tracing)
    }

    override fun mapReduce(clientSession: ClientSession, mapFunction: String, reduceFunction: String): TracingMapReduceFlux<T> {
        return delegate.mapReduce(clientSession, mapFunction, reduceFunction).toTracingReactor(tracing)
    }

    override fun <TResult : Any> mapReduce(
        clientSession: ClientSession,
        mapFunction: String,
        reduceFunction: String,
        clazz: Class<TResult>,
    ): TracingMapReduceFlux<TResult> {
        return delegate.mapReduce(clientSession, mapFunction, reduceFunction, clazz).toTracingReactor(tracing)
    }

    override fun bulkWrite(requests: List<WriteModel<out T>>): Mono<BulkWriteResult> = delegate.bulkWrite(requests).toTracingMono(tracing)

    override fun bulkWrite(requests: List<WriteModel<out T>>, options: BulkWriteOptions): Mono<BulkWriteResult> {
        return delegate.bulkWrite(requests, options).toTracingMono(tracing)
    }

    override fun bulkWrite(clientSession: ClientSession, requests: List<WriteModel<out T>>): Mono<BulkWriteResult> {
        return delegate.bulkWrite(clientSession, requests).toTracingMono(tracing)
    }

    override fun bulkWrite(
        clientSession: ClientSession,
        requests: List<WriteModel<out T>>,
        options: BulkWriteOptions,
    ): Mono<BulkWriteResult> {
        return delegate.bulkWrite(clientSession, requests, options).toTracingMono(tracing)
    }

    override fun insertOne(document: T): Mono<InsertOneResult> = delegate.insertOne(document).toTracingMono(tracing)

    override fun insertOne(document: T, options: InsertOneOptions): Mono<InsertOneResult> {
        return delegate.insertOne(document, options).toTracingMono(tracing)
    }

    override fun insertOne(clientSession: ClientSession, document: T): Mono<InsertOneResult> {
        return delegate.insertOne(clientSession, document).toTracingMono(tracing)
    }

    override fun insertOne(clientSession: ClientSession, document: T, options: InsertOneOptions): Mono<InsertOneResult> {
        return delegate.insertOne(clientSession, document, options).toTracingMono(tracing)
    }

    override fun insertMany(documents: List<T>): Mono<InsertManyResult> = delegate.insertMany(documents).toTracingMono(tracing)

    override fun insertMany(documents: List<T>, options: InsertManyOptions): Mono<InsertManyResult> {
        return delegate.insertMany(documents, options).toTracingMono(tracing)
    }

    override fun insertMany(clientSession: ClientSession, documents: List<T>): Mono<InsertManyResult> {
        return delegate.insertMany(clientSession, documents).toTracingMono(tracing)
    }

    override fun insertMany(
        clientSession: ClientSession,
        documents: List<T>,
        options: InsertManyOptions,
    ): Mono<InsertManyResult> {
        return delegate.insertMany(clientSession, documents, options).toTracingMono(tracing)
    }

    override fun deleteOne(filter: Bson): Mono<DeleteResult> = delegate.deleteOne(filter).toTracingMono(tracing)

    override fun deleteOne(filter: Bson, options: DeleteOptions): Mono<DeleteResult> =
        delegate.deleteOne(filter, options).toTracingMono(tracing)

    override fun deleteOne(clientSession: ClientSession, filter: Bson): Mono<DeleteResult> {
        return delegate.deleteOne(clientSession, filter).toTracingMono(tracing)
    }

    override fun deleteOne(clientSession: ClientSession, filter: Bson, options: DeleteOptions): Mono<DeleteResult> {
        return delegate.deleteOne(clientSession, filter, options).toTracingMono(tracing)
    }

    override fun deleteMany(filter: Bson): Mono<DeleteResult> = delegate.deleteMany(filter).toTracingMono(tracing)

    override fun deleteMany(filter: Bson, options: DeleteOptions): Mono<DeleteResult> {
        return delegate.deleteMany(filter, options).toTracingMono(tracing)
    }

    override fun deleteMany(clientSession: ClientSession, filter: Bson): Mono<DeleteResult> {
        return delegate.deleteMany(clientSession, filter).toTracingMono(tracing)
    }

    override fun deleteMany(clientSession: ClientSession, filter: Bson, options: DeleteOptions): Mono<DeleteResult> {
        return delegate.deleteMany(clientSession, filter, options).toTracingMono(tracing)
    }

    override fun replaceOne(filter: Bson, replacement: T): Mono<UpdateResult> =
        delegate.replaceOne(filter, replacement).toTracingMono(tracing)

    override fun replaceOne(filter: Bson, replacement: T, options: ReplaceOptions): Mono<UpdateResult> {
        return delegate.replaceOne(filter, replacement, options).toTracingMono(tracing)
    }

    override fun replaceOne(clientSession: ClientSession, filter: Bson, replacement: T): Mono<UpdateResult> {
        return delegate.replaceOne(clientSession, filter, replacement).toTracingMono(tracing)
    }

    override fun replaceOne(
        clientSession: ClientSession,
        filter: Bson,
        replacement: T,
        options: ReplaceOptions,
    ): Mono<UpdateResult> {
        return delegate.replaceOne(clientSession, filter, replacement, options).toTracingMono(tracing)
    }

    override fun updateOne(filter: Bson, update: Bson): Mono<UpdateResult> = delegate.updateOne(filter, update).toTracingMono(tracing)

    override fun updateOne(filter: Bson, update: Bson, options: UpdateOptions): Mono<UpdateResult> {
        return delegate.updateOne(filter, update, options).toTracingMono(tracing)
    }

    override fun updateOne(clientSession: ClientSession, filter: Bson, update: Bson): Mono<UpdateResult> {
        return delegate.updateOne(clientSession, filter, update).toTracingMono(tracing)
    }

    override fun updateOne(clientSession: ClientSession, filter: Bson, update: Bson, options: UpdateOptions): Mono<UpdateResult> {
        return delegate.updateOne(clientSession, filter, update, options).toTracingMono(tracing)
    }

    override fun updateOne(filter: Bson, update: List<Bson>): Mono<UpdateResult> {
        return delegate.updateOne(filter, update).toTracingMono(tracing)
    }

    override fun updateOne(filter: Bson, update: List<Bson>, options: UpdateOptions): Mono<UpdateResult> {
        return delegate.updateOne(filter, update, options).toTracingMono(tracing)
    }

    override fun updateOne(clientSession: ClientSession, filter: Bson, update: List<Bson>): Mono<UpdateResult> {
        return delegate.updateOne(clientSession, filter, update).toTracingMono(tracing)
    }

    override fun updateOne(
        clientSession: ClientSession,
        filter: Bson,
        update: List<Bson>,
        options: UpdateOptions,
    ): Mono<UpdateResult> {
        return delegate.updateOne(clientSession, filter, update, options).toTracingMono(tracing)
    }

    override fun updateMany(filter: Bson, update: Bson): Mono<UpdateResult> = delegate.updateMany(filter, update).toTracingMono(tracing)

    override fun updateMany(filter: Bson, update: Bson, options: UpdateOptions): Mono<UpdateResult> {
        return delegate.updateMany(filter, update, options).toTracingMono(tracing)
    }

    override fun updateMany(clientSession: ClientSession, filter: Bson, update: Bson): Mono<UpdateResult> {
        return delegate.updateMany(clientSession, filter, update).toTracingMono(tracing)
    }

    override fun updateMany(clientSession: ClientSession, filter: Bson, update: Bson, options: UpdateOptions): Mono<UpdateResult> {
        return delegate.updateMany(clientSession, filter, update, options).toTracingMono(tracing)
    }

    override fun updateMany(filter: Bson, update: List<Bson>): Mono<UpdateResult> {
        return delegate.updateMany(filter, update).toTracingMono(tracing)
    }

    override fun updateMany(filter: Bson, update: List<Bson>, options: UpdateOptions): Mono<UpdateResult> {
        return delegate.updateMany(filter, update, options).toTracingMono(tracing)
    }

    override fun updateMany(clientSession: ClientSession, filter: Bson, update: List<Bson>): Mono<UpdateResult> {
        return delegate.updateMany(clientSession, filter, update).toTracingMono(tracing)
    }

    override fun updateMany(
        clientSession: ClientSession,
        filter: Bson,
        update: List<Bson>,
        options: UpdateOptions,
    ): Mono<UpdateResult> {
        return delegate.updateMany(clientSession, filter, update, options).toTracingMono(tracing)
    }

    override fun findOneAndDelete(filter: Bson): Mono<T> = delegate.findOneAndDelete(filter).toTracingMono(tracing)

    override fun findOneAndDelete(filter: Bson, options: FindOneAndDeleteOptions): Mono<T> {
        return delegate.findOneAndDelete(filter, options).toTracingMono(tracing)
    }

    override fun findOneAndDelete(clientSession: ClientSession, filter: Bson): Mono<T> {
        return delegate.findOneAndDelete(clientSession, filter).toTracingMono(tracing)
    }

    override fun findOneAndDelete(clientSession: ClientSession, filter: Bson, options: FindOneAndDeleteOptions): Mono<T> {
        return delegate.findOneAndDelete(clientSession, filter, options).toTracingMono(tracing)
    }

    override fun findOneAndReplace(filter: Bson, replacement: T): Mono<T> {
        return delegate.findOneAndReplace(filter, replacement).toTracingMono(tracing)
    }

    override fun findOneAndReplace(filter: Bson, replacement: T, options: FindOneAndReplaceOptions): Mono<T> {
        return delegate.findOneAndReplace(filter, replacement, options).toTracingMono(tracing)
    }

    override fun findOneAndReplace(clientSession: ClientSession, filter: Bson, replacement: T): Mono<T> {
        return delegate.findOneAndReplace(clientSession, filter, replacement).toTracingMono(tracing)
    }

    override fun findOneAndReplace(
        clientSession: ClientSession,
        filter: Bson,
        replacement: T,
        options: FindOneAndReplaceOptions,
    ): Mono<T> {
        return delegate.findOneAndReplace(clientSession, filter, replacement, options).toTracingMono(tracing)
    }

    override fun findOneAndUpdate(filter: Bson, update: Bson): Mono<T> {
        return delegate.findOneAndUpdate(filter, update).toTracingMono(tracing)
    }

    override fun findOneAndUpdate(filter: Bson, update: Bson, options: FindOneAndUpdateOptions): Mono<T> {
        return delegate.findOneAndUpdate(filter, update, options).toTracingMono(tracing)
    }

    override fun findOneAndUpdate(clientSession: ClientSession, filter: Bson, update: Bson): Mono<T> {
        return delegate.findOneAndUpdate(clientSession, filter, update).toTracingMono(tracing)
    }

    override fun findOneAndUpdate(
        clientSession: ClientSession,
        filter: Bson,
        update: Bson,
        options: FindOneAndUpdateOptions,
    ): Mono<T> {
        return delegate.findOneAndUpdate(clientSession, filter, update, options).toTracingMono(tracing)
    }

    override fun findOneAndUpdate(filter: Bson, update: List<Bson>): Mono<T> {
        return delegate.findOneAndUpdate(filter, update).toTracingMono(tracing)
    }

    override fun findOneAndUpdate(filter: Bson, update: List<Bson>, options: FindOneAndUpdateOptions): Mono<T> {
        return delegate.findOneAndUpdate(filter, update, options).toTracingMono(tracing)
    }

    override fun findOneAndUpdate(clientSession: ClientSession, filter: Bson, update: List<Bson>): Mono<T> {
        return delegate.findOneAndUpdate(clientSession, filter, update).toTracingMono(tracing)
    }

    override fun findOneAndUpdate(
        clientSession: ClientSession,
        filter: Bson,
        update: List<Bson>,
        options: FindOneAndUpdateOptions,
    ): Mono<T> {
        return delegate.findOneAndUpdate(clientSession, filter, update, options).toTracingMono(tracing)
    }

    override fun drop(): Mono<Void> = delegate.drop().toTracingMono(tracing)

    override fun drop(clientSession: ClientSession): Mono<Void> = delegate.drop(clientSession).toTracingMono(tracing)

    override fun createIndex(key: Bson): Mono<String> = delegate.createIndex(key).toTracingMono(tracing)

    override fun createIndex(key: Bson, options: IndexOptions): Mono<String> = delegate.createIndex(key, options).toTracingMono(tracing)

    override fun createIndex(clientSession: ClientSession, key: Bson): Mono<String> {
        return delegate.createIndex(clientSession, key).toTracingMono(tracing)
    }

    override fun createIndex(clientSession: ClientSession, key: Bson, options: IndexOptions): Mono<String> {
        return delegate.createIndex(clientSession, key, options).toTracingMono(tracing)
    }

    override fun createIndexes(indexes: List<IndexModel>): Mono<String> = delegate.createIndexes(indexes).toTracingMono(tracing)

    override fun createIndexes(indexes: List<IndexModel>, createIndexOptions: CreateIndexOptions): Mono<String> {
        return delegate.createIndexes(indexes, createIndexOptions).toTracingMono(tracing)
    }

    override fun createIndexes(clientSession: ClientSession, indexes: List<IndexModel>): Mono<String> {
        return delegate.createIndexes(clientSession, indexes).toTracingMono(tracing)
    }

    override fun createIndexes(
        clientSession: ClientSession,
        indexes: List<IndexModel>,
        createIndexOptions: CreateIndexOptions,
    ): Mono<String> {
        return delegate.createIndexes(clientSession, indexes, createIndexOptions).toTracingMono(tracing)
    }

    override fun listIndexes(): TracingListIndexesFlux<Document> = delegate.listIndexes().toTracingReactor(tracing)

    override fun <TResult : Any> listIndexes(clazz: Class<TResult>): TracingListIndexesFlux<TResult> {
        return delegate.listIndexes(clazz).toTracingReactor(tracing)
    }

    override fun listIndexes(clientSession: ClientSession): TracingListIndexesFlux<Document> {
        return delegate.listIndexes(clientSession).toTracingReactor(tracing)
    }

    override fun <TResult : Any> listIndexes(clientSession: ClientSession, clazz: Class<TResult>): TracingListIndexesFlux<TResult> {
        return delegate.listIndexes(clientSession, clazz).toTracingReactor(tracing)
    }

    override fun dropIndex(indexName: String): Mono<Void> = delegate.dropIndex(indexName).toTracingMono(tracing)

    override fun dropIndex(keys: Bson): Mono<Void> = delegate.dropIndex(keys).toTracingMono(tracing)

    override fun dropIndex(indexName: String, dropIndexOptions: DropIndexOptions): Mono<Void> {
        return delegate.dropIndex(indexName, dropIndexOptions).toTracingMono(tracing)
    }

    override fun dropIndex(keys: Bson, dropIndexOptions: DropIndexOptions): Mono<Void> {
        return delegate.dropIndex(keys, dropIndexOptions).toTracingMono(tracing)
    }

    override fun dropIndex(clientSession: ClientSession, indexName: String): Mono<Void> {
        return delegate.dropIndex(clientSession, indexName).toTracingMono(tracing)
    }

    override fun dropIndex(clientSession: ClientSession, keys: Bson): Mono<Void> {
        return delegate.dropIndex(clientSession, keys).toTracingMono(tracing)
    }

    override fun dropIndex(clientSession: ClientSession, indexName: String, dropIndexOptions: DropIndexOptions): Mono<Void> {
        return delegate.dropIndex(clientSession, indexName, dropIndexOptions).toTracingMono(tracing)
    }

    override fun dropIndex(clientSession: ClientSession, keys: Bson, dropIndexOptions: DropIndexOptions): Mono<Void> {
        return delegate.dropIndex(clientSession, keys, dropIndexOptions).toTracingMono(tracing)
    }

    override fun dropIndexes(): Mono<Void> = delegate.dropIndexes().toTracingMono(tracing)

    override fun dropIndexes(dropIndexOptions: DropIndexOptions): Mono<Void> = delegate.dropIndexes(dropIndexOptions).toTracingMono(tracing)

    override fun dropIndexes(clientSession: ClientSession): Mono<Void> = delegate.dropIndexes(clientSession).toTracingMono(tracing)

    override fun dropIndexes(clientSession: ClientSession, dropIndexOptions: DropIndexOptions): Mono<Void> {
        return delegate.dropIndexes(clientSession, dropIndexOptions).toTracingMono(tracing)
    }

    override fun renameCollection(newCollectionNamespace: MongoNamespace): Mono<Void> {
        return delegate.renameCollection(newCollectionNamespace).toTracingMono(tracing)
    }

    override fun renameCollection(newCollectionNamespace: MongoNamespace, options: RenameCollectionOptions): Mono<Void> {
        return delegate.renameCollection(newCollectionNamespace, options).toTracingMono(tracing)
    }

    override fun renameCollection(clientSession: ClientSession, newCollectionNamespace: MongoNamespace): Mono<Void> {
        return delegate.renameCollection(clientSession, newCollectionNamespace).toTracingMono(tracing)
    }

    override fun renameCollection(
        clientSession: ClientSession,
        newCollectionNamespace: MongoNamespace,
        options: RenameCollectionOptions,
    ): Mono<Void> {
        return delegate.renameCollection(clientSession, newCollectionNamespace, options).toTracingMono(tracing)
    }
}
