import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val mongoDriverVersion = "4.9.0"
val reactorVersion = "2020.0.21"
val mockkVersion = "1.12.3"
val junitVersion = "5.9.0"
val assertJVersion = "3.23.1"
val testContainersVersion = "1.17.3"

plugins {
    id("org.jetbrains.dokka") version "1.7.10"
    `java-library`
    `maven-publish`
    signing
    jacoco
}

dependencies {
    api(platform("io.projectreactor:reactor-bom:$reactorVersion"))
    api("io.projectreactor:reactor-core")
    api("org.mongodb:mongodb-driver-reactivestreams:$mongoDriverVersion")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
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
    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.required.set(true)
        }
    }
    check {
        dependsOn(jacocoTestReport)
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
                issueManagement {
                    system.set("Github issues")
                    url.set("https://github.com/jntakpe/mongo-reactor-adapter/issues")
                }
                ciManagement {
                    system.set("Github actions")
                    url.set("https://github.com/jntakpe/mongo-reactor-adapter/actions")
                }
                scm {
                    connection.set("scm:git:git@github.com:jntakpe/mongo-reactor-adapter.git")
                    developerConnection.set("scm:git:git@github.com:jntakpe/mongo-reactor-adapter.git")
                    url.set("https://github.com/jntakpe/mongo-reactor-adapter/")
                }
            }
        }
    }
    repositories {
        maven {
            name = "Maven_central"
            setUrl("https://oss.sonatype.org/service/local/staging/deploy/maven2")
            credentials {
                val sonatypeUsername: String? by project
                val sonatypePassword: String? by project
                username = sonatypeUsername
                password = sonatypePassword
            }
        }
        maven {
            name = "Github_packages"
            setUrl("https://maven.pkg.github.com/jntakpe/mongo-reactor-adapter")
            credentials {
                val githubActor: String? by project
                val githubToken: String? by project
                username = githubActor
                password = githubToken
            }
        }
    }
}

signing {
    if ("CI" in System.getenv()) {
        val signingKey: String? by project
        val signingPassword: String? by project
        useInMemoryPgpKeys(signingKey, signingPassword)
    }
    sign(publishing.publications["mavenJava"])
}
