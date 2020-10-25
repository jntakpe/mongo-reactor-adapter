import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val reactorVersion: String by project
val mongoDriverVersion: String by project
val junitVersion: String by project
val mockkVersion: String by project
val assertJVersion: String by project
val testContainersVersion: String by project

plugins {
    kotlin("jvm") version "1.4.10"
    id("org.jetbrains.dokka") version "1.4.10.2"
    `java-library`
    `maven-publish`
    signing
}

group = "com.github.jntakpe"
version = "0.1.0"

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
    dokkaJavadoc.configure {
        outputDirectory.set(buildDir.resolve("javadoc"))
    }
    javadoc {
        dependsOn(dokkaJavadoc)
        setDestinationDir(buildDir.resolve("javadoc"))
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components.findByName("java"))
            pom {
                name.set(project.name)
                description.set("MongoDB's Reactor adapter for ReactiveStreams driver")
                url.set("https://github.com/jntakpe/mongo-reactor-adapter")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("jntakpe")
                        name.set("Jocelyn NTAKPE")
                    }
                }
                scm {
                    connection.set("scm:git:git@github.com:jntakpe/mongo-reactor-adapter.git")
                    developerConnection.set("scm:git:git@github.com:jntakpe/mongo-reactor-adapter.git")
                    url.set("https://github.com/jntakpe/mongo-reactor-adapter/")
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}
