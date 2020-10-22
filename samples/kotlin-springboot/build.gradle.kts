import org.springframework.boot.gradle.plugin.SpringBootPlugin

val testContainersVersion: String by project
val kMongoVersion: String = "4.1.3"

plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.spring") version "1.3.72"
    id("org.springframework.boot") version "2.3.4.RELEASE"
}

dependencies {
    implementation(project(":mongo-reactor-adapter"))
    implementation(kotlin("stdlib"))
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
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
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            javaParameters = true
        }
    }
}

springBoot {
    mainClassName = "com.mongodb.reactor.client.sample.kotlin.springboot.JavaSpringBoot"
}
