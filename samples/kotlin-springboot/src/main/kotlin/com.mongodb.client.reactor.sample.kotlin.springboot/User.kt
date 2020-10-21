package com.mongodb.client.reactor.sample.kotlin.springboot

import org.bson.types.ObjectId

data class User(val email: String, val firstName: String, val lastName: String, val id: ObjectId = ObjectId()) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        return email.hashCode()
    }

    override fun toString(): String {
        return "User(email='$email', firstName='$firstName', lastName='$lastName')"
    }
}

