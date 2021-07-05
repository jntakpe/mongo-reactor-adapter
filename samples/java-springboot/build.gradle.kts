import org.springframework.boot.gradle.plugin.SpringBootPlugin

val testContainersVersion: String by rootProject.extra

plugins {
    java
    id("org.springframework.boot") version "2.5.2"
}

dependencies {
    implementation(project(":mongo-reactor-adapter"))
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.testcontainers:testcontainers:$testContainersVersion")
    testImplementation("org.testcontainers:mongodb:$testContainersVersion")
}

tasks {
    test {
        useJUnitPlatform()
    }
}

springBoot {
    mainClass.set("com.mongodb.reactor.client.sample.java.springboot.JavaSpringBoot")
}

