import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val reactorVersion: String by project
val mongoDriverVersion: String by project
val junitVersion: String by project
val mockkVersion: String by project
val assertJVersion: String by project
val testContainersVersion: String by project

plugins {
    kotlin("jvm") version "1.4.10"
    `java-library`
    `maven-publish`
}

group = "com.github.jntakpe"
version = "0.1.0-SNAPSHOT"

dependencies {
    api(platform("io.projectreactor:reactor-bom:$reactorVersion"))
    api("io.projectreactor:reactor-core")
    api("io.projectreactor.kotlin:reactor-kotlin-extensions")
    api("org.mongodb:mongodb-driver-reactivestreams:$mongoDriverVersion")
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("reflect"))
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.assertj:assertj-core:$assertJVersion")
    testImplementation("org.testcontainers:testcontainers:$testContainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testContainersVersion")
    testImplementation("org.testcontainers:mongodb:$testContainersVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
    withJavadocJar()
}

kotlin {
    explicitApiWarning()
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    test {
        useJUnitPlatform()
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components.findByName("java"))
        }
    }
}
