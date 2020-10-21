package com.mongodb.reactor.client

import com.mongodb.MongoNamespace
import com.mongodb.ReadConcern
import com.mongodb.ReadPreference
import com.mongodb.WriteConcern
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
import com.mongodb.client.model.InsertOneModel
import com.mongodb.client.model.InsertOneOptions
import com.mongodb.client.model.RenameCollectionOptions
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.client.model.UpdateOptions
import com.mongodb.reactivestreams.client.ClientSession
import com.mongodb.reactivestreams.client.MongoCollection
import com.mongodb.reactor.client.MongoContainer.COLLECTION_NAME
import com.mongodb.reactor.client.MongoContainer.DATABASE_NAME
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifyAll
import org.bson.BsonDocument
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistry
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.kotlin.test.test

internal class ReactorMongoCollectionTest {

    private val original = mockk<MongoCollection<Document>>(relaxed = true)
    private val reactor = spyk(ReactorMongoCollection(original))
    private val collection = MongoContainer.database.getCollection(COLLECTION_NAME).toReactor()

    @BeforeEach
    fun init() {
        MongoContainer.initData()
    }

    @AfterEach
    fun clean() {
        MongoContainer.clearData()
    }

    @Test
    fun `reactor collection should always delegate method calls to the underlying collection`() {
        reactor.withDocumentClass(Document::class.java)
        val registry = mockk<CodecRegistry>(relaxed = true)
        reactor.withCodecRegistry(registry)
        reactor.withReadPreference(ReadPreference.nearest())
        reactor.withWriteConcern(WriteConcern.W1)
        reactor.withReadConcern(ReadConcern.LOCAL)
        reactor.estimatedDocumentCount()
        val estimatedDocumentCountOptions = EstimatedDocumentCountOptions()
        reactor.estimatedDocumentCount(estimatedDocumentCountOptions)
        reactor.countDocuments()
        val countOptions = CountOptions()
        val bsonDocument = BsonDocument()
        reactor.countDocuments(bsonDocument)
        reactor.countDocuments(bsonDocument, countOptions)
        val session = mockk<ClientSession>()
        reactor.countDocuments(session)
        reactor.countDocuments(session, bsonDocument)
        reactor.countDocuments(session, bsonDocument, countOptions)
        val usernameAttribute = "username"
        reactor.distinct(usernameAttribute, Document::class.java)
        reactor.distinct(usernameAttribute, bsonDocument, Document::class.java)
        reactor.distinct(session, usernameAttribute, Document::class.java)
        reactor.distinct(session, usernameAttribute, bsonDocument, Document::class.java)
        reactor.find()
        reactor.find(Document::class.java)
        reactor.find(bsonDocument)
        reactor.find(bsonDocument, Document::class.java)
        reactor.find(session)
        reactor.find(session, Document::class.java)
        reactor.find(session, bsonDocument)
        reactor.find(session, bsonDocument, Document::class.java)
        val pipeline = listOf(bsonDocument)
        reactor.aggregate(pipeline)
        reactor.aggregate(pipeline, Document::class.java)
        reactor.aggregate(session, pipeline)
        reactor.aggregate(session, pipeline, Document::class.java)
        reactor.watch()
        reactor.watch(Document::class.java)
        reactor.watch(pipeline)
        reactor.watch(pipeline, Document::class.java)
        reactor.watch(session)
        reactor.watch(session, Document::class.java)
        reactor.watch(session, pipeline)
        reactor.watch(session, pipeline, Document::class.java)
        val mapFunction = "function map() { emit(1, this.age) }"
        val reduceFunction = "function reduce(key, values) { return Array.sum(values) }"
        reactor.mapReduce(mapFunction, reduceFunction)
        reactor.mapReduce(mapFunction, reduceFunction, Float::class.java)
        reactor.mapReduce(session, mapFunction, reduceFunction)
        reactor.mapReduce(session, mapFunction, reduceFunction, Float::class.java)
        val document = Document().append("username", "new")
        val writeRequests = listOf(InsertOneModel(document))
        reactor.bulkWrite(writeRequests)
        val bulkWriteOptions = BulkWriteOptions()
        reactor.bulkWrite(writeRequests, bulkWriteOptions)
        reactor.bulkWrite(session, writeRequests)
        reactor.bulkWrite(session, writeRequests, bulkWriteOptions)
        reactor.insertOne(document)
        val insertOneOptions = InsertOneOptions()
        reactor.insertOne(document, insertOneOptions)
        reactor.insertOne(session, document)
        reactor.insertOne(session, document, insertOneOptions)
        val documents = listOf(document)
        reactor.insertMany(documents)
        val insertManyOptions = InsertManyOptions()
        reactor.insertMany(documents, insertManyOptions)
        reactor.insertMany(session, documents)
        reactor.insertMany(session, documents, insertManyOptions)
        val oldDocument = Document().append("username", "Jean")
        reactor.deleteOne(oldDocument)
        val deleteOptions = DeleteOptions()
        reactor.deleteOne(oldDocument, deleteOptions)
        reactor.deleteOne(session, oldDocument)
        reactor.deleteOne(session, oldDocument, deleteOptions)
        reactor.deleteMany(oldDocument)
        reactor.deleteMany(oldDocument, deleteOptions)
        reactor.deleteMany(session, oldDocument)
        reactor.deleteMany(session, oldDocument, deleteOptions)
        reactor.replaceOne(oldDocument, document)
        val replaceOptions = ReplaceOptions()
        reactor.replaceOne(oldDocument, document, replaceOptions)
        reactor.replaceOne(session, oldDocument, document)
        reactor.replaceOne(session, oldDocument, document, replaceOptions)
        reactor.updateOne(oldDocument, document)
        val updateOptions = UpdateOptions()
        reactor.updateOne(oldDocument, document)
        reactor.updateOne(oldDocument, document, updateOptions)
        reactor.updateOne(session, oldDocument, document)
        reactor.updateOne(session, oldDocument, document, updateOptions)
        reactor.updateOne(oldDocument, documents)
        reactor.updateOne(oldDocument, documents, updateOptions)
        reactor.updateOne(session, oldDocument, documents)
        reactor.updateOne(session, oldDocument, documents, updateOptions)
        reactor.updateMany(oldDocument, document)
        reactor.updateMany(oldDocument, document, updateOptions)
        reactor.updateMany(session, oldDocument, document)
        reactor.updateMany(session, oldDocument, document, updateOptions)
        reactor.updateMany(oldDocument, documents)
        reactor.updateMany(oldDocument, documents, updateOptions)
        reactor.updateMany(session, oldDocument, documents)
        reactor.updateMany(session, oldDocument, documents, updateOptions)
        reactor.findOneAndDelete(oldDocument)
        val findOneAndDeleteOptions = FindOneAndDeleteOptions()
        reactor.findOneAndDelete(oldDocument, findOneAndDeleteOptions)
        reactor.findOneAndDelete(session, oldDocument)
        reactor.findOneAndDelete(session, oldDocument, findOneAndDeleteOptions)
        reactor.findOneAndReplace(oldDocument, document)
        val findOneAndReplaceOptions = FindOneAndReplaceOptions()
        reactor.findOneAndReplace(oldDocument, document, findOneAndReplaceOptions)
        reactor.findOneAndReplace(session, oldDocument, document)
        reactor.findOneAndReplace(session, oldDocument, document, findOneAndReplaceOptions)
        reactor.findOneAndUpdate(oldDocument, document)
        val findOneAndUpdateOptions = FindOneAndUpdateOptions()
        reactor.findOneAndUpdate(oldDocument, document, findOneAndUpdateOptions)
        reactor.findOneAndUpdate(session, oldDocument, document)
        reactor.findOneAndUpdate(session, oldDocument, document, findOneAndUpdateOptions)
        reactor.findOneAndUpdate(oldDocument, documents)
        reactor.findOneAndUpdate(oldDocument, documents, findOneAndUpdateOptions)
        reactor.findOneAndUpdate(session, oldDocument, documents)
        reactor.findOneAndUpdate(session, oldDocument, documents, findOneAndUpdateOptions)
        reactor.drop()
        reactor.drop(session)
        reactor.createIndex(document)
        val indexOptions = IndexOptions()
        reactor.createIndex(document, indexOptions)
        reactor.createIndex(session, document)
        reactor.createIndex(session, document, indexOptions)
        val indexModel = listOf(IndexModel(document))
        reactor.createIndexes(indexModel)
        val createIndexOptions = CreateIndexOptions()
        reactor.createIndexes(indexModel, createIndexOptions)
        reactor.createIndexes(session, indexModel)
        reactor.createIndexes(session, indexModel, createIndexOptions)
        reactor.listIndexes()
        reactor.listIndexes(Document::class.java)
        reactor.listIndexes(session)
        reactor.listIndexes(session, Document::class.java)
        val indexName = "index"
        reactor.dropIndex(indexName)
        reactor.dropIndex(document)
        val dropIndexOptions = DropIndexOptions()
        reactor.dropIndex(indexName, dropIndexOptions)
        reactor.dropIndex(document, dropIndexOptions)
        reactor.dropIndex(session, indexName)
        reactor.dropIndex(session, document)
        reactor.dropIndex(session, indexName, dropIndexOptions)
        reactor.dropIndex(session, document, dropIndexOptions)
        reactor.dropIndexes()
        reactor.dropIndexes(dropIndexOptions)
        reactor.dropIndexes(session)
        reactor.dropIndexes(session, dropIndexOptions)
        val namespace = MongoNamespace("$DATABASE_NAME.$COLLECTION_NAME")
        reactor.renameCollection(namespace)
        val renameCollectionOptions = RenameCollectionOptions()
        reactor.renameCollection(namespace, renameCollectionOptions)
        reactor.renameCollection(session, namespace)
        reactor.renameCollection(session, namespace, renameCollectionOptions)
        verifyAll {
            original.withDocumentClass(Document::class.java)
            original.withCodecRegistry(registry)
            original.withReadPreference(ReadPreference.nearest())
            original.withWriteConcern(WriteConcern.W1)
            original.withReadConcern(ReadConcern.LOCAL)
            original.estimatedDocumentCount()
            original.estimatedDocumentCount(estimatedDocumentCountOptions)
            original.countDocuments()
            original.countDocuments(bsonDocument)
            original.countDocuments(bsonDocument, countOptions)
            original.countDocuments(session)
            original.countDocuments(session, bsonDocument)
            original.countDocuments(session, bsonDocument, countOptions)
            original.distinct(usernameAttribute, Document::class.java)
            original.distinct(usernameAttribute, bsonDocument, Document::class.java)
            original.distinct(session, usernameAttribute, Document::class.java)
            original.distinct(session, usernameAttribute, bsonDocument, Document::class.java)
            original.find()
            original.find(Document::class.java)
            original.find(bsonDocument)
            original.find(bsonDocument, Document::class.java)
            original.find(session)
            original.find(session, Document::class.java)
            original.find(session, bsonDocument)
            original.find(session, bsonDocument, Document::class.java)
            original.aggregate(pipeline)
            original.aggregate(pipeline, Document::class.java)
            original.aggregate(session, pipeline)
            original.aggregate(session, pipeline, Document::class.java)
            original.watch()
            original.watch(Document::class.java)
            original.watch(pipeline)
            original.watch(pipeline, Document::class.java)
            original.watch(session)
            original.watch(session, Document::class.java)
            original.watch(session, pipeline)
            original.watch(session, pipeline, Document::class.java)
            original.mapReduce(mapFunction, reduceFunction)
            original.mapReduce(mapFunction, reduceFunction, Float::class.java)
            original.mapReduce(session, mapFunction, reduceFunction)
            original.mapReduce(session, mapFunction, reduceFunction, Float::class.java)
            original.bulkWrite(writeRequests)
            original.bulkWrite(writeRequests, bulkWriteOptions)
            original.bulkWrite(session, writeRequests)
            original.bulkWrite(session, writeRequests, bulkWriteOptions)
            original.insertOne(document)
            original.insertOne(document, insertOneOptions)
            original.insertOne(session, document)
            original.insertOne(session, document, insertOneOptions)
            original.insertMany(documents)
            original.insertMany(documents, insertManyOptions)
            original.insertMany(session, documents)
            original.insertMany(session, documents, insertManyOptions)
            original.deleteOne(oldDocument)
            original.deleteOne(oldDocument, deleteOptions)
            original.deleteOne(session, oldDocument)
            original.deleteOne(session, oldDocument, deleteOptions)
            original.deleteMany(oldDocument)
            original.deleteMany(oldDocument, deleteOptions)
            original.deleteMany(session, oldDocument)
            original.deleteMany(session, oldDocument, deleteOptions)
            original.replaceOne(oldDocument, document)
            original.replaceOne(oldDocument, document, replaceOptions)
            original.replaceOne(session, oldDocument, document)
            original.replaceOne(session, oldDocument, document, replaceOptions)
            original.updateOne(oldDocument, document)
            original.updateOne(oldDocument, document)
            original.updateOne(oldDocument, document, updateOptions)
            original.updateOne(session, oldDocument, document)
            original.updateOne(session, oldDocument, document, updateOptions)
            original.updateOne(oldDocument, documents)
            original.updateOne(oldDocument, documents, updateOptions)
            original.updateOne(session, oldDocument, documents)
            original.updateOne(session, oldDocument, documents, updateOptions)
            original.updateMany(oldDocument, document)
            original.updateMany(oldDocument, document, updateOptions)
            original.updateMany(session, oldDocument, document)
            original.updateMany(session, oldDocument, document, updateOptions)
            original.updateMany(oldDocument, documents)
            original.updateMany(oldDocument, documents, updateOptions)
            original.updateMany(session, oldDocument, documents)
            original.updateMany(session, oldDocument, documents, updateOptions)
            original.findOneAndDelete(oldDocument)
            original.findOneAndDelete(oldDocument, findOneAndDeleteOptions)
            original.findOneAndDelete(session, oldDocument)
            original.findOneAndDelete(session, oldDocument, findOneAndDeleteOptions)
            original.findOneAndReplace(oldDocument, document)
            original.findOneAndReplace(oldDocument, document, findOneAndReplaceOptions)
            original.findOneAndReplace(session, oldDocument, document)
            original.findOneAndReplace(session, oldDocument, document, findOneAndReplaceOptions)
            original.findOneAndUpdate(oldDocument, document)
            original.findOneAndUpdate(oldDocument, document, findOneAndUpdateOptions)
            original.findOneAndUpdate(session, oldDocument, document)
            original.findOneAndUpdate(session, oldDocument, document, findOneAndUpdateOptions)
            original.findOneAndUpdate(oldDocument, documents)
            original.findOneAndUpdate(oldDocument, documents, findOneAndUpdateOptions)
            original.findOneAndUpdate(session, oldDocument, documents)
            original.findOneAndUpdate(session, oldDocument, documents, findOneAndUpdateOptions)
            original.drop()
            original.drop(session)
            original.createIndex(document)
            original.createIndex(document, indexOptions)
            original.createIndex(session, document)
            original.createIndex(session, document, indexOptions)
            original.createIndexes(indexModel)
            original.createIndexes(indexModel, createIndexOptions)
            original.createIndexes(session, indexModel)
            original.createIndexes(session, indexModel, createIndexOptions)
            original.listIndexes()
            original.listIndexes(Document::class.java)
            original.listIndexes(session)
            original.listIndexes(session, Document::class.java)
            original.dropIndex(indexName)
            original.dropIndex(document)
            original.dropIndex(indexName, dropIndexOptions)
            original.dropIndex(document, dropIndexOptions)
            original.dropIndex(session, indexName)
            original.dropIndex(session, document)
            original.dropIndex(session, indexName, dropIndexOptions)
            original.dropIndex(session, document, dropIndexOptions)
            original.dropIndexes()
            original.dropIndexes(dropIndexOptions)
            original.dropIndexes(session)
            original.dropIndexes(session, dropIndexOptions)
            original.renameCollection(namespace)
            original.renameCollection(namespace, renameCollectionOptions)
            original.renameCollection(session, namespace)
            original.renameCollection(session, namespace, renameCollectionOptions)
        }
        confirmVerified(original)
    }

    @Test
    fun `reactor collection should emit items`() {
        collection
            .find()
            .map { it["username"] }
            .test()
            .expectSubscription()
            .expectNext("Jean")
            .expectNext("Pierre")
            .expectNext("Tom")
            .verifyComplete()
    }
}