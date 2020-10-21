package com.mongodb.client.reactor.sample.java.micronaut;

import io.micronaut.runtime.Micronaut;

public class JavaMicronaut {

    public static void main(String[] args) {
        Micronaut.run(JavaMicronaut.class, args).start();
    }
}
