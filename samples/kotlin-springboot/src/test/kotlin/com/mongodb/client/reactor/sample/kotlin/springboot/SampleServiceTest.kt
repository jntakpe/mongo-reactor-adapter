package com.mongodb.client.reactor.sample.kotlin.springboot

import com.mongodb.reactivestreams.client.MongoClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import reactor.kotlin.core.publisher.toMono
import reactor.kotlin.test.test

@SpringBootTest
internal class SampleServiceTest(@Autowired private val service: SampleService, @Autowired private val client: MongoClient) {

    companion object {

        private const val DB_NAME = "test"
        private val container = MongoDBContainer("mongo:4.4").apply { start() }

        @JvmStatic
        @DynamicPropertySource
        fun databaseProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.mongodb.port") { container.firstMappedPort }
        }
    }

    @BeforeEach
    fun setup() {
        val collection = client.getDatabase(DB_NAME).getCollection("users", User::class.java)
        collection.insertMany(
            listOf(
                User("john.doe@mail.com", "John", "Doe"),
                User("jane.doe@mail.com", "Jane", "Doe")
            )
        ).toMono().block()
    }

    @AfterEach
    fun cleanUp() {
        client.getDatabase(DB_NAME).getCollection("users").drop().toMono().block()
    }

    @Test
    fun `list databases names should return test db name`() {
        service.listDatabasesNames().test()
            .expectSubscription()
            .recordWith { ArrayList() }
            .thenConsumeWhile { true }
            .consumeRecordedWith {
                assertThat(it.size).isGreaterThan(1)
                assertThat(it).contains(DB_NAME)
            }
            .verifyComplete()
    }

    @Test
    fun listCollectionsShouldReturnUsersCollection() {
        service.listCollections().test()
            .expectSubscription()
            .expectNext("users")
            .verifyComplete()
    }

    @Test
    fun findAllShouldReturnMultipleUsers() {
        service.findAll().test()
            .expectSubscription()
            .recordWith { ArrayList() }
            .expectNextCount(2)
            .consumeRecordedWith { s -> assertThat(s.stream().map { it.firstName }).contains("John", "Jane") }
            .verifyComplete()
    }

    @Test
    fun createShouldAddNewDocument() {
        val user = User("mark.doe@mail.com", "Mark", "Doe")
        service.create(user).test()
            .expectSubscription()
            .consumeNextWith { assertThat(it).usingRecursiveComparison().isEqualTo(user) }
            .verifyComplete()
    }
}
