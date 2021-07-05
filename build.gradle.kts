val mongoDriverVersion by extra("4.2.0")
val reactorVersion by extra("2020.0.4")
val braveVersion by extra("5.13.3")
val mockkVersion by extra("1.10.6")
val junitVersion by extra("5.7.1")
val assertJVersion by extra("3.19.0")
val testContainersVersion by extra("1.15.2")

plugins {
    kotlin("jvm") version "1.5.20"
}


subprojects {
    group = "com.github.jntakpe"
    version = "0.2.0"

    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
        jcenter()
    }
}
