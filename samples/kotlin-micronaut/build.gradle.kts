import io.micronaut.gradle.MicronautRuntime
import io.micronaut.gradle.MicronautTestRuntime.JUNIT_5
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kMongoVersion = "4.5.1"
val micronautVersion = "3.5.3"
val reactorVersion = "2020.0.21"
val assertJVersion = "3.23.1"
val testContainersVersion = "1.17.3"

plugins {
    kotlin("kapt")
    kotlin("plugin.allopen") version "1.6.21"
    id("io.micronaut.application") version "3.7.0"
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
    kapt(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    kapt("io.micronaut:micronaut-inject-java:$micronautVersion")
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



