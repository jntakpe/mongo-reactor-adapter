include(":library")
include(":tracing")
include("samples:java-springboot")
include("samples:java-micronaut")
include("samples:kotlin-micronaut")
include("samples:kotlin-springboot")

project(":library").name = "mongo-reactor-adapter"
project(":tracing").name = "tracing-mongo-reactor-adapter"
