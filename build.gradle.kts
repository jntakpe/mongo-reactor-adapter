plugins {
    kotlin("jvm") version "1.5.10"
}

subprojects {
    group = "com.github.jntakpe"
    version = "0.2.1"

    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
    }
}
