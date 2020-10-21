package com.mongodb.client.reactor.sample.kotlin.springboot

import com.mongodb.MongoClientSettings
import org.litote.kmongo.service.ClassMappingType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class KotlinSpringBoot {

    @Bean
    fun kotlinCodecs() = MongoClientSettingsBuilderCustomizer {
        it.codecRegistry(ClassMappingType.codecRegistry(MongoClientSettings.getDefaultCodecRegistry()))
    }

    fun main(args: Array<String>) {
        runApplication<KotlinSpringBoot>(*args)
    }
}
