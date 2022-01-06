package com.mongodb.reactor.client.sample.java.springboot;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SampleServiceTest {

    private static final String DB_NAME = "test";

    private static final MongoDBContainer container = initContainer();

    @Autowired
    private SampleService service;

    @Autowired
    private MongoClient client;

    static MongoDBContainer initContainer() {
        MongoDBContainer container = new MongoDBContainer("mongo:4.4");
        container.start();
        return container;
    }

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.port", container::getFirstMappedPort);
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
