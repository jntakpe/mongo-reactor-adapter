import io.micronaut.gradle.MicronautRuntime
import io.micronaut.gradle.MicronautTestRuntime.JUNIT_5

val micronautVersion = "3.0.0"
val reactorVersion = "2020.0.10"
val assertJVersion = "3.20.2"
val testContainersVersion = "1.16.0"

plugins {
    java
    id("io.micronaut.application") version "3.1.1"
}

micronaut {
    version(micronautVersion)
    runtime(MicronautRuntime.NETTY)
    testRuntime(JUNIT_5)
    processing {
        incremental(true)
        annotations("com.mongodb.client.reactor.sample.*")
    }
}

dependencies {
    implementation(project(":mongo-reactor-adapter"))
    implementation(platform("io.projectreactor:reactor-bom:$reactorVersion"))
    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.mongodb:micronaut-mongo-reactive")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.assertj:assertj-core:$assertJVersion")
    testImplementation("org.testcontainers:testcontainers:$testContainersVersion")
    testImplementation("org.testcontainers:mongodb:$testContainersVersion")
}

application {
    mainClass.set("com.mongodb.client.reactor.sample.MicronautApplication")
}




