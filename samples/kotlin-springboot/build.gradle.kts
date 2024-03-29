import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.plugin.SpringBootPlugin

val kMongoVersion = "4.5.1"
val testContainersVersion = "1.17.3"

plugins {
    kotlin("plugin.spring") version "1.6.21"
    id("org.springframework.boot") version "2.7.2"
}

dependencies {
    implementation(project(":mongo-reactor-adapter"))
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.litote.kmongo:kmongo-native-mapping:$kMongoVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.testcontainers:testcontainers:$testContainersVersion")
    testImplementation("org.testcontainers:mongodb:$testContainersVersion")
}

tasks {
    test {
        useJUnitPlatform()
    }
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            javaParameters = true
        }
    }
}

springBoot {
    mainClass.set("com.mongodb.reactor.client.sample.kotlin.springboot.JavaSpringBoot")
}
