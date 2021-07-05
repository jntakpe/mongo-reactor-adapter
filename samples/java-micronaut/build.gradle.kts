import io.micronaut.gradle.MicronautRuntime
import io.micronaut.gradle.MicronautTestRuntime.JUNIT_5

val reactorVersion: String by rootProject.extra
val testContainersVersion: String by rootProject.extra
val assertJVersion: String by rootProject.extra

plugins {
    java
    id("io.micronaut.application") version "2.0.2"
}

micronaut {
    version("2.3.0")
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




