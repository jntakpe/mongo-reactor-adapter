package com.mongodb.client.reactor.sample.java.micronaut;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactor.client.MongoReactorKt;
import com.mongodb.reactor.client.ReactorMongoClient;
import com.mongodb.reactor.client.ReactorMongoCollection;
import com.mongodb.reactor.client.ReactorMongoDatabase;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Singleton;

@Singleton
public class SampleService {

    private final ReactorMongoClient reactorClient;

    private final ReactorMongoDatabase reactorDb;

    private final ReactorMongoCollection<User> reactorCollection;

    public SampleService(MongoClient reactiveStreamsClient) {
        reactorClient = MongoReactorKt.toReactor(reactiveStreamsClient);
        reactorDb = reactorClient.getDatabase("test");
        // or reactorDb = MongoReactorKt.toReactor(reactiveStreamsClient.getDatabase("test"));
        reactorCollection = reactorDb.getCollection("users", User.class);
        // or reactorCollection = MongoReactorKt.toReactor(reactiveStreamsClient.getDatabase("test").getCollection("users", User.class));
    }

    public Flux<String> listDatabasesNames() {
        return reactorClient.listDatabaseNames();
    }

    public Flux<String> listCollections() {
        return reactorDb.listCollectionNames();
    }

    public Flux<User> findAll() {
        return reactorCollection.find();
    }

    public Mono<User> create(User user) {
        return reactorCollection.insertOne(user).thenReturn(user);
    }

}
