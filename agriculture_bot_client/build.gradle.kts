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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1")
    implementation("org.litote.kmongo:kmongo-serialization:4.2.3")
    implementation("org.litote.kmongo:kmongo-id-serialization:4.2.3")
}


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}