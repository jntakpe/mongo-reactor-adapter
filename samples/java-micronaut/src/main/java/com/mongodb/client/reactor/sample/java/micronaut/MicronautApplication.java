package com.mongodb.client.reactor.sample.java.micronaut;

import io.micronaut.runtime.Micronaut;

public class MicronautApplication {

    public static void main(String[] args) {
        Micronaut.run(MicronautApplication.class, args).start();
    }
}
