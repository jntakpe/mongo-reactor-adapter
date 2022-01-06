![build](https://github.com/jntakpe/mongo-reactor-adapter/workflows/build/badge.svg) 
[![codecov](https://codecov.io/gh/jntakpe/mongo-reactor-adapter/branch/master/graph/badge.svg?token=5YCPKI2X1K)](https://codecov.io/gh/jntakpe/mongo-reactor-adapter) 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.jntakpe/mongo-reactor-adapter/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.jntakpe/mongo-reactor-adapter)
![license](https://img.shields.io/github/license/jntakpe/mongo-reactor-adapter)

# Java MongoDB's Reactor adapter

Simple yet effective [Reactor](https://projectreactor.io) adapter for MongoDB's 
[reactivestreams driver](http://mongodb.github.io/mongo-java-driver/), written in Kotlin, works perfectly for both Java and Kotlin.

## Installation

### Requirements

* Java 1.8 or later

### Gradle users

Add this dependency to your project's build file :

###### Groovy script

````groovy
implementation "com.github.jntakpe:mongo-reactor-adapter:0.3.1"
````

###### Kotlin script

```kotlin
implementation("com.github.jntakpe:mongo-reactor-adapter:0.3.1")
```

### Maven users

```xml

<dependency>
    <groupId>com.github.jntakpe</groupId>
    <artifactId>mongo-reactor-adapter</artifactId>
    <version>0.3.1</version>
</dependency>
```

## Usage

Wrappers are provided for main Reactive Streams MongoDB driver class including 
[MongoClient](driver-reactive-streams/src/main/com/mongodb/reactivestreams/client/MongoClient.java),
[MongoCollection](https://github.com/mongodb/mongo-java-driver/blob/master/driver-reactive-streams/src/main/com/mongodb/reactivestreams/client/MongoCollection.java) 
and [MongoDatabase](https://github.com/mongodb/mongo-java-driver/blob/master/driver-reactive-streams/src/main/com/mongodb/reactivestreams/client/MongoDatabase.java).
Return types are wrapped using Reactor's [Mono](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Mono.html), 
[Flux](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Flux.html) or compatible subtypes like 
[AggregateFlux](library/src/main/kotlin/com/mongodb/reactor/client/AggregateFlux.kt).

### Java example

```java
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
```

### Kotlin example

```kotlin
class SampleService(reactiveStreamsClient: MongoClient) {

    private val reactorClient: ReactorMongoClient = reactiveStreamsClient.toReactor()
    private val reactorDb: ReactorMongoDatabase = reactorClient.getDatabase("test")
    // or private val reactorDb = reactiveStreamsClient.getDatabase("test").toReactor()
    private val reactorCollection: ReactorMongoCollection<User> = reactorDb.getCollection("users", User::class.java)
    // or private val reactorCollection = reactiveStreamsClient.getDatabase("test").getCollection("users", User::class.java).toReactor()

    fun listDatabasesNames(): Flux<String> = reactorClient.listDatabaseNames()
    
    fun listCollections(): Flux<String> = reactorDb.listCollectionNames()
    
    fun findAll(): FindFlux<User> = reactorCollection.find()
    
    fun create(user: User): Mono<User> = reactorCollection.insertOne(user).thenReturn(user)
}
```

Both Spring Boot and Micronaut samples written using Java and Kotlin are available in the [samples](samples) directory.

## Why MongoDB reactor adapter

MongoDB Reactive Streams Java driver API is a great way to leverage MongoDB's asynchronous capabilities. Nevertheless, the scope of 
Reactive Streams is to find a minimal set of interfaces, methods and protocols that will describe the necessary operations 
and entities to achieve the goal—asynchronous streams of data with non-blocking back pressure.
End-user DSLs or protocol binding APIs have purposefully been left out of the reactivestream's scope to encourage and enable different implementations 
that potentially use different programming languages to stay as true as possible to the idioms of their platform.
However, Reactor offers composable asynchronous sequence APIs - Flux (for [N] elements) and Mono (for [0|1] elements) - 
and extensively implements the Reactive Streams specification providing an efficient way for end user to create reactive system.

MongoDB's reactor adapter combines both MongoDB Reactive Streams Java driver API's together with Reactor to offer the best of two worlds.
No more wrapping required, you can directly leverage Reactor's operators power when querying your Mongo database.
