package com.mongodb.client.reactor.sample.java.micronaut;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.MongoDBContainer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SampleServiceTest implements TestPropertyProvider {

    private static final String DB_NAME = "test";

    private static final MongoDBContainer container = initContainer();

    @Inject
    private SampleService service;

    @Inject
    private MongoClient client;

    static MongoDBContainer initContainer() {
        MongoDBContainer container = new MongoDBContainer("mongo:4.4");
        container.start();
        return container;
    }

    @NotNull
    @Override
    public Map<String, String> getProperties() {
        return Collections.singletonMap("MONGO_PORT", container.getFirstMappedPort().toString());
    }

    @BeforeEach
    void setup() {
        MongoCollection<User> collection = client.getDatabase(DB_NAME).getCollection("users", User.class);
        Mono.from(collection.insertMany(Stream.of(
                new User().setFirstName("John").setLastName("Doe").setEmail("john.doe@mail.com"),
                new User().setFirstName("Jane").setLastName("Doe").setEmail("jane.doe@mail.com")
        ).collect(Collectors.toList()))).block();
    }

    @AfterEach
    void cleanUp() {
        Mono.from(client.getDatabase(DB_NAME).getCollection("users").drop()).block();
    }

    @Test
    void listDatabasesNamesShouldReturnTestDbName() {
        StepVerifier.create(service.listDatabasesNames())
                    .expectSubscription()
                    .recordWith(ArrayList::new)
                    .thenConsumeWhile(s -> true)
                    .consumeRecordedWith(l -> {
                        assertThat(l.size()).isGreaterThan(1);
                        assertThat(l).contains(DB_NAME);
                    })
                    .verifyComplete();
    }

    @Test
    void listCollectionsShouldReturnUsersCollection() {
        StepVerifier.create(service.listCollections())
                    .expectSubscription()
                    .expectNext("users")
                    .verifyComplete();
    }

    @Test
    void findAllShouldReturnMultipleUsers() {
        StepVerifier.create(service.findAll())
                    .expectSubscription()
                    .recordWith(ArrayList::new)
                    .expectNextCount(2)
                    .consumeRecordedWith(l -> assertThat(l.stream().map(User::getFirstName)).contains("John", "Jane"))
                    .verifyComplete();
    }

    @Test
    void createShouldAddNewDocument() {
        User user = new User().setFirstName("Mark").setLastName("Doe").setEmail("mark.doe@gmail.com").setId(new ObjectId());
        StepVerifier.create(service.create(user))
                    .expectSubscription()
                    .consumeNextWith(u -> assertThat(u).usingRecursiveComparison().isEqualTo(user))
                    .verifyComplete();
    }

}
