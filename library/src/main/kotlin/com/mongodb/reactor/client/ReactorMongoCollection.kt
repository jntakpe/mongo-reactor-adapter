package com.mongodb.reactor.client

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
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistry
import org.bson.conversions.Bson
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

public class ReactorMongoCollection<T>(private val delegate: MongoCollection<T>) : MongoCollection<T> by delegate {

    override fun <NewTDocument : Any> withDocumentClass(clazz: Class<NewTDocument>): ReactorMongoCollection<NewTDocument> {
        return ReactorMongoCollection(delegate.withDocumentClass(clazz))
    }

    override fun withCodecRegistry(codecRegistry: CodecRegistry): ReactorMongoCollection<T> {
        return ReactorMongoCollection(delegate.withCodecRegistry(codecRegistry))
    }

    override fun withReadPreference(readPreference: ReadPreference): ReactorMongoCollection<T> {
        return ReactorMongoCollection(delegate.withReadPreference(readPreference))
    }

    override fun withWriteConcern(writeConcern: WriteConcern): ReactorMongoCollection<T> {
        return ReactorMongoCollection(delegate.withWriteConcern(writeConcern))
    }

    override fun withReadConcern(readConcern: ReadConcern): ReactorMongoCollection<T> {
        return ReactorMongoCollection(delegate.withReadConcern(readConcern))
    }

    override fun estimatedDocumentCount(): Mono<Long> = delegate.estimatedDocumentCount().toMono()

    override fun estimatedDocumentCount(options: EstimatedDocumentCountOptions): Mono<Long> {
        return delegate.estimatedDocumentCount(options).toMono()
    }

    override fun countDocuments(): Mono<Long> = delegate.countDocuments().toMono()

    override fun countDocuments(filter: Bson): Mono<Long> = delegate.countDocuments(filter).toMono()

    override fun countDocuments(filter: Bson, options: CountOptions): Mono<Long> = delegate.countDocuments(filter, options).toMono()

    override fun countDocuments(clientSession: ClientSession): Mono<Long> = delegate.countDocuments(clientSession).toMono()

    override fun countDocuments(clientSession: ClientSession, filter: Bson): Mono<Long> {
        return delegate.countDocuments(clientSession, filter).toMono()
    }

    override fun countDocuments(clientSession: ClientSession, filter: Bson, options: CountOptions): Mono<Long> {
        return delegate.countDocuments(clientSession, filter, options).toMono()
    }

    override fun <TResult : Any> distinct(fieldName: String, resultClass: Class<TResult>): DistinctFlux<TResult> {
        return delegate.distinct(fieldName, resultClass).toReactor()
    }

    override fun <TResult : Any> distinct(fieldName: String, filter: Bson, resultClass: Class<TResult>): DistinctFlux<TResult> {
        return delegate.distinct(fieldName, filter, resultClass).toReactor()
    }

    override fun <TResult : Any> distinct(
        clientSession: ClientSession,
        fieldName: String,
        resultClass: Class<TResult>
    ): DistinctFlux<TResult> {
        return delegate.distinct(clientSession, fieldName, resultClass).toReactor()
    }

    override fun <TResult : Any> distinct(
        clientSession: ClientSession,
        fieldName: String,
        filter: Bson,
        resultClass: Class<TResult>
    ): DistinctFlux<TResult> {
        return delegate.distinct(clientSession, fieldName, filter, resultClass).toReactor()
    }

    override fun find(): FindFlux<T> = delegate.find().toReactor()

    override fun <TResult : Any> find(clazz: Class<TResult>): FindFlux<TResult> = delegate.find(clazz).toReactor()

    override fun find(filter: Bson): FindFlux<T> = delegate.find(filter).toReactor()

    override fun <TResult : Any> find(filter: Bson, clazz: Class<TResult>): FindFlux<TResult> = delegate.find(filter, clazz).toReactor()

    override fun find(clientSession: ClientSession): FindFlux<T> = delegate.find(clientSession).toReactor()

    override fun <TResult : Any> find(clientSession: ClientSession, clazz: Class<TResult>): FindFlux<TResult> {
        return delegate.find(clientSession, clazz).toReactor()
    }

    override fun find(clientSession: ClientSession, filter: Bson): FindFlux<T> {
        return delegate.find(clientSession, filter).toReactor()
    }

    override fun <TResult : Any> find(clientSession: ClientSession, filter: Bson, clazz: Class<TResult>): FindFlux<TResult> {
        return delegate.find(clientSession, filter, clazz).toReactor()
    }

    override fun aggregate(pipeline: List<Bson>): AggregateFlux<T> = delegate.aggregate(pipeline).toReactor()

    override fun <TResult : Any> aggregate(pipeline: List<Bson>, clazz: Class<TResult>): AggregateFlux<TResult> {
        return delegate.aggregate(pipeline, clazz).toReactor()
    }

    override fun aggregate(clientSession: ClientSession, pipeline: List<Bson>): AggregateFlux<T> {
        return delegate.aggregate(clientSession, pipeline).toReactor()
    }

    override fun <TResult : Any> aggregate(
        clientSession: ClientSession,
        pipeline: List<Bson>,
        clazz: Class<TResult>
    ): AggregateFlux<TResult> {
        return delegate.aggregate(clientSession, pipeline, clazz).toReactor()
    }

    override fun watch(): ChangeStreamFlux<Document> = delegate.watch().toReactor()

    override fun <TResult : Any> watch(resultClass: Class<TResult>): ChangeStreamFlux<TResult> = delegate.watch(resultClass).toReactor()

    override fun watch(pipeline: List<Bson>): ChangeStreamFlux<Document> = delegate.watch(pipeline).toReactor()

    override fun <TResult : Any> watch(pipeline: List<Bson>, resultClass: Class<TResult>): ChangeStreamFlux<TResult> {
        return delegate.watch(pipeline, resultClass).toReactor()
    }

    override fun watch(clientSession: ClientSession): ChangeStreamFlux<Document> = delegate.watch(clientSession).toReactor()

    override fun <TResult : Any> watch(clientSession: ClientSession, resultClass: Class<TResult>): ChangeStreamFlux<TResult> {
        return delegate.watch(clientSession, resultClass).toReactor()
    }

    override fun watch(clientSession: ClientSession, pipeline: List<Bson>): ChangeStreamFlux<Document> {
        return delegate.watch(clientSession, pipeline).toReactor()
    }

    override fun <TResult : Any> watch(
        clientSession: ClientSession,
        pipeline: List<Bson>,
        resultClass: Class<TResult>
    ): ChangeStreamFlux<TResult> {
        return delegate.watch(clientSession, pipeline, resultClass).toReactor()
    }

    override fun mapReduce(mapFunction: String, reduceFunction: String): MapReduceFlux<T> {
        return delegate.mapReduce(mapFunction, reduceFunction).toReactor()
    }

    override fun <TResult : Any> mapReduce(
        mapFunction: String,
        reduceFunction: String,
        clazz: Class<TResult>
    ): MapReduceFlux<TResult> {
        return delegate.mapReduce(mapFunction, reduceFunction, clazz).toReactor()
    }

    override fun mapReduce(clientSession: ClientSession, mapFunction: String, reduceFunction: String): MapReduceFlux<T> {
        return delegate.mapReduce(clientSession, mapFunction, reduceFunction).toReactor()
    }

    override fun <TResult : Any> mapReduce(
        clientSession: ClientSession,
        mapFunction: String,
        reduceFunction: String,
        clazz: Class<TResult>
    ): MapReduceFlux<TResult> {
        return delegate.mapReduce(clientSession, mapFunction, reduceFunction, clazz).toReactor()
    }

    override fun bulkWrite(requests: List<WriteModel<out T>>): Mono<BulkWriteResult> = delegate.bulkWrite(requests).toMono()

    override fun bulkWrite(requests: List<WriteModel<out T>>, options: BulkWriteOptions): Mono<BulkWriteResult> {
        return delegate.bulkWrite(requests, options).toMono()
    }

    override fun bulkWrite(clientSession: ClientSession, requests: List<WriteModel<out T>>): Mono<BulkWriteResult> {
        return delegate.bulkWrite(clientSession, requests).toMono()
    }

    override fun bulkWrite(
        clientSession: ClientSession,
        requests: List<WriteModel<out T>>,
        options: BulkWriteOptions
    ): Mono<BulkWriteResult> {
        return delegate.bulkWrite(clientSession, requests, options).toMono()
    }

    override fun insertOne(document: T): Mono<InsertOneResult> = delegate.insertOne(document).toMono()

    override fun insertOne(document: T, options: InsertOneOptions): Mono<InsertOneResult> = delegate.insertOne(document, options).toMono()

    override fun insertOne(clientSession: ClientSession, document: T): Mono<InsertOneResult> {
        return delegate.insertOne(clientSession, document).toMono()
    }

    override fun insertOne(clientSession: ClientSession, document: T, options: InsertOneOptions): Mono<InsertOneResult> {
        return delegate.insertOne(clientSession, document, options).toMono()
    }

    override fun insertMany(documents: List<T>): Mono<InsertManyResult> = delegate.insertMany(documents).toMono()

    override fun insertMany(documents: List<T>, options: InsertManyOptions): Mono<InsertManyResult> {
        return delegate.insertMany(documents, options).toMono()
    }

    override fun insertMany(clientSession: ClientSession, documents: List<T>): Mono<InsertManyResult> {
        return delegate.insertMany(clientSession, documents).toMono()
    }

    override fun insertMany(
        clientSession: ClientSession,
        documents: List<T>,
        options: InsertManyOptions
    ): Mono<InsertManyResult> {
        return delegate.insertMany(clientSession, documents, options).toMono()
    }

    override fun deleteOne(filter: Bson): Mono<DeleteResult> = delegate.deleteOne(filter).toMono()

    override fun deleteOne(filter: Bson, options: DeleteOptions): Mono<DeleteResult> = delegate.deleteOne(filter, options).toMono()

    override fun deleteOne(clientSession: ClientSession, filter: Bson): Mono<DeleteResult> {
        return delegate.deleteOne(clientSession, filter).toMono()
    }

    override fun deleteOne(clientSession: ClientSession, filter: Bson, options: DeleteOptions): Mono<DeleteResult> {
        return delegate.deleteOne(clientSession, filter, options).toMono()
    }

    override fun deleteMany(filter: Bson): Mono<DeleteResult> = delegate.deleteMany(filter).toMono()

    override fun deleteMany(filter: Bson, options: DeleteOptions): Mono<DeleteResult> = delegate.deleteMany(filter, options).toMono()

    override fun deleteMany(clientSession: ClientSession, filter: Bson): Mono<DeleteResult> {
        return delegate.deleteMany(clientSession, filter).toMono()
    }

    override fun deleteMany(clientSession: ClientSession, filter: Bson, options: DeleteOptions): Mono<DeleteResult> {
        return delegate.deleteMany(clientSession, filter, options).toMono()
    }

    override fun replaceOne(filter: Bson, replacement: T): Mono<UpdateResult> = delegate.replaceOne(filter, replacement).toMono()

    override fun replaceOne(filter: Bson, replacement: T, options: ReplaceOptions): Mono<UpdateResult> {
        return delegate.replaceOne(filter, replacement, options).toMono()
    }

    override fun replaceOne(clientSession: ClientSession, filter: Bson, replacement: T): Mono<UpdateResult> {
        return delegate.replaceOne(clientSession, filter, replacement).toMono()
    }

    override fun replaceOne(
        clientSession: ClientSession,
        filter: Bson,
        replacement: T,
        options: ReplaceOptions
    ): Mono<UpdateResult> {
        return delegate.replaceOne(clientSession, filter, replacement, options).toMono()
    }

    override fun updateOne(filter: Bson, update: Bson): Mono<UpdateResult> = delegate.updateOne(filter, update).toMono()

    override fun updateOne(filter: Bson, update: Bson, options: UpdateOptions): Mono<UpdateResult> {
        return delegate.updateOne(filter, update, options).toMono()
    }

    override fun updateOne(clientSession: ClientSession, filter: Bson, update: Bson): Mono<UpdateResult> {
        return delegate.updateOne(clientSession, filter, update).toMono()
    }

    override fun updateOne(clientSession: ClientSession, filter: Bson, update: Bson, options: UpdateOptions): Mono<UpdateResult> {
        return delegate.updateOne(clientSession, filter, update, options).toMono()
    }

    override fun updateOne(filter: Bson, update: List<Bson>): Mono<UpdateResult> {
        return delegate.updateOne(filter, update).toMono()
    }

    override fun updateOne(filter: Bson, update: List<Bson>, options: UpdateOptions): Mono<UpdateResult> {
        return delegate.updateOne(filter, update, options).toMono()
    }

    override fun updateOne(clientSession: ClientSession, filter: Bson, update: List<Bson>): Mono<UpdateResult> {
        return delegate.updateOne(clientSession, filter, update).toMono()
    }

    override fun updateOne(
        clientSession: ClientSession,
        filter: Bson,
        update: List<Bson>,
        options: UpdateOptions
    ): Mono<UpdateResult> {
        return delegate.updateOne(clientSession, filter, update, options).toMono()
    }

    override fun updateMany(filter: Bson, update: Bson): Mono<UpdateResult> = delegate.updateMany(filter, update).toMono()

    override fun updateMany(filter: Bson, update: Bson, options: UpdateOptions): Mono<UpdateResult> {
        return delegate.updateMany(filter, update, options).toMono()
    }

    override fun updateMany(clientSession: ClientSession, filter: Bson, update: Bson): Mono<UpdateResult> {
        return delegate.updateMany(clientSession, filter, update).toMono()
    }

    override fun updateMany(clientSession: ClientSession, filter: Bson, update: Bson, options: UpdateOptions): Mono<UpdateResult> {
        return delegate.updateMany(clientSession, filter, update, options).toMono()
    }

    override fun updateMany(filter: Bson, update: List<Bson>): Mono<UpdateResult> {
        return delegate.updateMany(filter, update).toMono()
    }

    override fun updateMany(filter: Bson, update: List<Bson>, options: UpdateOptions): Mono<UpdateResult> {
        return delegate.updateMany(filter, update, options).toMono()
    }

    override fun updateMany(clientSession: ClientSession, filter: Bson, update: List<Bson>): Mono<UpdateResult> {
        return delegate.updateMany(clientSession, filter, update).toMono()
    }

    override fun updateMany(
        clientSession: ClientSession,
        filter: Bson,
        update: List<Bson>,
        options: UpdateOptions
    ): Mono<UpdateResult> {
        return delegate.updateMany(clientSession, filter, update, options).toMono()
    }

    override fun findOneAndDelete(filter: Bson): Mono<T> = delegate.findOneAndDelete(filter).toMono()

    override fun findOneAndDelete(filter: Bson, options: FindOneAndDeleteOptions): Mono<T> {
        return delegate.findOneAndDelete(filter, options).toMono()
    }

    override fun findOneAndDelete(clientSession: ClientSession, filter: Bson): Mono<T> {
        return delegate.findOneAndDelete(clientSession, filter).toMono()
    }

    override fun findOneAndDelete(clientSession: ClientSession, filter: Bson, options: FindOneAndDeleteOptions): Mono<T> {
        return delegate.findOneAndDelete(clientSession, filter, options).toMono()
    }

    override fun findOneAndReplace(filter: Bson, replacement: T): Mono<T> {
        return delegate.findOneAndReplace(filter, replacement).toMono()
    }

    override fun findOneAndReplace(filter: Bson, replacement: T, options: FindOneAndReplaceOptions): Mono<T> {
        return delegate.findOneAndReplace(filter, replacement, options).toMono()
    }

    override fun findOneAndReplace(clientSession: ClientSession, filter: Bson, replacement: T): Mono<T> {
        return delegate.findOneAndReplace(clientSession, filter, replacement).toMono()
    }

    override fun findOneAndReplace(
        clientSession: ClientSession,
        filter: Bson,
        replacement: T,
        options: FindOneAndReplaceOptions
    ): Mono<T> {
        return delegate.findOneAndReplace(clientSession, filter, replacement, options).toMono()
    }

    override fun findOneAndUpdate(filter: Bson, update: Bson): Mono<T> {
        return delegate.findOneAndUpdate(filter, update).toMono()
    }

    override fun findOneAndUpdate(filter: Bson, update: Bson, options: FindOneAndUpdateOptions): Mono<T> {
        return delegate.findOneAndUpdate(filter, update, options).toMono()
    }

    override fun findOneAndUpdate(clientSession: ClientSession, filter: Bson, update: Bson): Mono<T> {
        return delegate.findOneAndUpdate(clientSession, filter, update).toMono()
    }

    override fun findOneAndUpdate(
        clientSession: ClientSession,
        filter: Bson,
        update: Bson,
        options: FindOneAndUpdateOptions
    ): Mono<T> {
        return delegate.findOneAndUpdate(clientSession, filter, update, options).toMono()
    }

    override fun findOneAndUpdate(filter: Bson, update: List<Bson>): Mono<T> {
        return delegate.findOneAndUpdate(filter, update).toMono()
    }

    override fun findOneAndUpdate(filter: Bson, update: List<Bson>, options: FindOneAndUpdateOptions): Mono<T> {
        return delegate.findOneAndUpdate(filter, update, options).toMono()
    }

    override fun findOneAndUpdate(clientSession: ClientSession, filter: Bson, update: List<Bson>): Mono<T> {
        return delegate.findOneAndUpdate(clientSession, filter, update).toMono()
    }

    override fun findOneAndUpdate(
        clientSession: ClientSession,
        filter: Bson,
        update: List<Bson>,
        options: FindOneAndUpdateOptions
    ): Mono<T> {
        return delegate.findOneAndUpdate(clientSession, filter, update, options).toMono()
    }

    override fun drop(): Mono<Void> = delegate.drop().toMono()

    override fun drop(clientSession: ClientSession): Mono<Void> = delegate.drop(clientSession).toMono()

    override fun createIndex(key: Bson): Mono<String> = delegate.createIndex(key).toMono()

    override fun createIndex(key: Bson, options: IndexOptions): Mono<String> = delegate.createIndex(key, options).toMono()

    override fun createIndex(clientSession: ClientSession, key: Bson): Mono<String> {
        return delegate.createIndex(clientSession, key).toMono()
    }

    override fun createIndex(clientSession: ClientSession, key: Bson, options: IndexOptions): Mono<String> {
        return delegate.createIndex(clientSession, key, options).toMono()
    }

    override fun createIndexes(indexes: List<IndexModel>): Mono<String> = delegate.createIndexes(indexes).toMono()

    override fun createIndexes(indexes: List<IndexModel>, createIndexOptions: CreateIndexOptions): Mono<String> {
        return delegate.createIndexes(indexes, createIndexOptions).toMono()
    }

    override fun createIndexes(clientSession: ClientSession, indexes: List<IndexModel>): Mono<String> {
        return delegate.createIndexes(clientSession, indexes).toMono()
    }

    override fun createIndexes(
        clientSession: ClientSession,
        indexes: List<IndexModel>,
        createIndexOptions: CreateIndexOptions
    ): Mono<String> {
        return delegate.createIndexes(clientSession, indexes, createIndexOptions).toMono()
    }

    override fun listIndexes(): ListIndexesFlux<Document> = delegate.listIndexes().toReactor()

    override fun <TResult : Any> listIndexes(clazz: Class<TResult>): ListIndexesFlux<TResult> = delegate.listIndexes(clazz).toReactor()

    override fun listIndexes(clientSession: ClientSession): ListIndexesFlux<Document> = delegate.listIndexes(clientSession).toReactor()

    override fun <TResult : Any> listIndexes(clientSession: ClientSession, clazz: Class<TResult>): ListIndexesFlux<TResult> {
        return delegate.listIndexes(clientSession, clazz).toReactor()
    }

    override fun dropIndex(indexName: String): Mono<Void> = delegate.dropIndex(indexName).toMono()

    override fun dropIndex(keys: Bson): Mono<Void> = delegate.dropIndex(keys).toMono()

    override fun dropIndex(indexName: String, dropIndexOptions: DropIndexOptions): Mono<Void> {
        return delegate.dropIndex(indexName, dropIndexOptions).toMono()
    }

    override fun dropIndex(keys: Bson, dropIndexOptions: DropIndexOptions): Mono<Void> {
        return delegate.dropIndex(keys, dropIndexOptions).toMono()
    }

    override fun dropIndex(clientSession: ClientSession, indexName: String): Mono<Void> {
        return delegate.dropIndex(clientSession, indexName).toMono()
    }

    override fun dropIndex(clientSession: ClientSession, keys: Bson): Mono<Void> {
        return delegate.dropIndex(clientSession, keys).toMono()
    }

    override fun dropIndex(clientSession: ClientSession, indexName: String, dropIndexOptions: DropIndexOptions): Mono<Void> {
        return delegate.dropIndex(clientSession, indexName, dropIndexOptions).toMono()
    }

    override fun dropIndex(clientSession: ClientSession, keys: Bson, dropIndexOptions: DropIndexOptions): Mono<Void> {
        return delegate.dropIndex(clientSession, keys, dropIndexOptions).toMono()
    }

    override fun dropIndexes(): Mono<Void> = delegate.dropIndexes().toMono()

    override fun dropIndexes(dropIndexOptions: DropIndexOptions): Mono<Void> = delegate.dropIndexes(dropIndexOptions).toMono()

    override fun dropIndexes(clientSession: ClientSession): Mono<Void> = delegate.dropIndexes(clientSession).toMono()

    override fun dropIndexes(clientSession: ClientSession, dropIndexOptions: DropIndexOptions): Mono<Void> {
        return delegate.dropIndexes(clientSession, dropIndexOptions).toMono()
    }

    override fun renameCollection(newCollectionNamespace: MongoNamespace): Mono<Void> {
        return delegate.renameCollection(newCollectionNamespace).toMono()
    }

    override fun renameCollection(newCollectionNamespace: MongoNamespace, options: RenameCollectionOptions): Mono<Void> {
        return delegate.renameCollection(newCollectionNamespace, options).toMono()
    }

    override fun renameCollection(clientSession: ClientSession, newCollectionNamespace: MongoNamespace): Mono<Void> {
        return delegate.renameCollection(clientSession, newCollectionNamespace).toMono()
    }

    override fun renameCollection(
        clientSession: ClientSession,
        newCollectionNamespace: MongoNamespace,
        options: RenameCollectionOptions
    ): Mono<Void> {
        return delegate.renameCollection(clientSession, newCollectionNamespace, options).toMono()
    }
}
