plugins {
    `java-library`
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.serialization") version "1.4.10"
}

group;"agriculture_bot"
version;"1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    //testCompile("group: 'junit', name: 'junit', version: '4.12'") ToDo: is this required? It caused an error while building.
    implementation("org.zeromq:jeromq:0.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
}