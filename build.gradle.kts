plugins {
    kotlin("jvm") version "1.7.21"
}

subprojects {
    group = "com.github.jntakpe"
    version = "0.3.2"

    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
    }
}
