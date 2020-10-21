package com.mongodb.client.reactor.sample.kotlin.micronaut

import io.micronaut.runtime.Micronaut

fun main(args: Array<String>) {
    Micronaut.build()
        .args(*args)
        .packages("com.mongodb.client.reactor.sample.kotlin.micronaut")
        .start()
}
