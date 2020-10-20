package com.mongodb.reactor.client.sample.java.springboot;

import com.mongodb.MongoClientSettings;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@SpringBootApplication
public class JavaSpringBoot {

    public static void main(String[] args) {
        SpringApplication.run(JavaSpringBoot.class, args);
    }

    @Bean
    public MongoClientSettingsBuilderCustomizer mongoCodec() {
        return b -> b.codecRegistry(fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                                                   fromProviders(PojoCodecProvider.builder().automatic(true).build())));
    }
}
