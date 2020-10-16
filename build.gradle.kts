val reactorVersion: String by project
val mongoDriverVersion: String by project

plugins {
    kotlin("jvm") version "1.4.10"
    `java-library`
}

group = "com.github.jntakpe"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(platform("io.projectreactor:reactor-bom:$reactorVersion"))
    api("io.projectreactor:reactor-core")
    api("io.projectreactor.kotlin:reactor-kotlin-extensions")
    api("org.mongodb:mongodb-driver-reactivestreams:$mongoDriverVersion")
    implementation(kotlin("stdlib"))
}

java {
    withSourcesJar()
    withJavadocJar()
}
