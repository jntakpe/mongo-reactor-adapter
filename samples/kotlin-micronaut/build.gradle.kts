import io.micronaut.gradle.MicronautRuntime
import io.micronaut.gradle.MicronautTestRuntime.JUNIT_5
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val reactorVersion: String by project
val testContainersVersion: String by project
val assertJVersion: String by project
val kMongoVersion: String = "4.2.4"

plugins {
    val kotlinVersion = "1.4.30"
    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion
    kotlin("plugin.allopen") version kotlinVersion
    id("io.micronaut.application") version "1.0.3"
}

micronaut {
    version("2.1.1")
    runtime(MicronautRuntime.NETTY)
    testRuntime(JUNIT_5)
    processing {
        incremental(true)
        annotations("com.mongodb.client.reactor.sample.*")
    }
}

dependencies {
    kapt(platform("io.micronaut:micronaut-bom"))
    kapt("io.micronaut:micronaut-inject-java")
    implementation(project(":mongo-reactor-adapter"))
    implementation(platform("io.projectreactor:reactor-bom:$reactorVersion"))
    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.mongodb:micronaut-mongo-reactive")
    implementation("org.litote.kmongo:kmongo-native-mapping:$kMongoVersion")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    kaptTest(platform("io.micronaut:micronaut-bom"))
    kaptTest("io.micronaut:micronaut-inject-java")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.assertj:assertj-core:$assertJVersion")
    testImplementation("org.testcontainers:testcontainers:$testContainersVersion")
    testImplementation("org.testcontainers:mongodb:$testContainersVersion")
}

application {
    mainClass.set("com.mongodb.client.reactor.sample.MicronautApplication")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            javaParameters = true
        }
    }
}



