package com.mongodb.client.reactor.sample.kotlin.micronaut

import com.mongodb.MongoClientSettings
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import org.litote.kmongo.service.ClassMappingType

@Factory
class MongoConfig {

    @Singleton
    fun kotlinCodecs() = ClassMappingType.codecRegistry(MongoClientSettings.getDefaultCodecRegistry())
}
