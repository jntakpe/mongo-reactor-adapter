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
    jacoco
}

group = "com.github.jntakpe"
version = "0.1.2-RC2"

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
    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.isEnabled = true
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
            fun repositoryUrl(): String {
                val repositoryBase = "https://oss.sonatype.org"
                return if (project.version.toString().endsWith("SNAPSHOT")) {
                    "$repositoryBase/content/repositories/snapshots"
                } else {
                    "$repositoryBase/service/local/staging/deploy/maven2"
                }
            }
            setUrl(repositoryUrl())
            credentials {
                val sonatypeUsername: String? by extra
                val sonatypePassword: String? by extra
                username = sonatypeUsername
                password = sonatypePassword
            }
        }
    }
}

signing {
    if ("CI" in System.getenv()) {
        val signingKey: String? by project
        val signingKeyPassword: String? by project
        useInMemoryPgpKeys(signingKey, signingKeyPassword)
    }
    sign(publishing.publications["mavenJava"])
}
