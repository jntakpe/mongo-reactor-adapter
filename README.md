# Java MongoDB's Reactor adapter

Simple yet effective [Reactor](https://projectreactor.io) adapter for MongoDB's 
[reactivestreams driver](http://mongodb.github.io/mongo-java-driver/), written in Kotlin.

## Installation

### Requirements

* Java 1.8 or later

### Gradle users

Add this dependency to your project's build file :

###### Groovy script

````groovy
implementation "com.github.jntakpe:mongo-reactor-adapter:1.0.0-SNAPSHOT"
````

###### Kotlin script

```kotlin
implementation("com.github.jntakpe:mongo-reactor-adapter:1.0.0-SNAPSHOT")
```

### Maven users

```xml
<dependency>
  <groupId>com.github.jntakpe</groupId>
  <artifactId>mongo-reactor-adapter</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Usage

Wrappers are provided for main Reactive Streams MongoDB driver class including 
[MongoClient](driver-reactive-streams/src/main/com/mongodb/reactivestreams/client/MongoClient.java),
[MongoCollection](https://github.com/mongodb/mongo-java-driver/blob/master/driver-reactive-streams/src/main/com/mongodb/reactivestreams/client/MongoCollection.java) 
and [MongoDatabase](https://github.com/mongodb/mongo-java-driver/blob/master/driver-reactive-streams/src/main/com/mongodb/reactivestreams/client/MongoDatabase.java).
Return types are wrapped using Reactor's [Mono](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Mono.html), 
[Flux](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Flux.html) or compatible subtypes like 
[AggregateFlux](src/main/kotlin/com/mongodb/reactor/client/AggregateFlux.kt).

### Java example

TODO

### Kotlin example

TODO

## Why MongoDB reactor adapter

MongoDB Reactive Streams Java driver API is a great way to leverage MongoDB's asynchronous capabilities. Nevertheless, the scope of 
Reactive Streams is to find a minimal set of interfaces, methods and protocols that will describe the necessary operations 
and entities to achieve the goal—asynchronous streams of data with non-blocking back pressure.
End-user DSLs or protocol binding APIs have purposefully been left out of the scope to encourage and enable different implementations 
that potentially use different programming languages to stay as true as possible to the idioms of their platform.
However, Reactor offers composable asynchronous sequence APIs - Flux (for [N] elements) and Mono (for [0|1] elements) - 
and extensively implements the Reactive Streams specification providing an efficient way for end user to create reactive system.

The goal of MongoDB reactor adapter is to offer the best reactive experience to MongoDB Java users thought Reactor's APIs.


