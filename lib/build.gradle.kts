plugins {
    id("org.jetbrains.kotlin.jvm") version "2.1.21"
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    // Logging
    compileOnly("io.github.oshai:kotlin-logging-jvm:7.0.7")

    // Exposed ORM
    val exposedVersion = "0.61.0"
    compileOnly("org.jetbrains.exposed:exposed-core:$exposedVersion")
    compileOnly("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    compileOnly("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    compileOnly("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    compileOnly("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")
}

java {
    withSourcesJar()
}

publishing {
    val libVersion = "3.8.0"

    publications {
        create<MavenPublication>("defaultJar") {
            groupId = "com.github.suwayomi"
            artifactId = "exposed-migrations"
            version = libVersion

            from(components["kotlin"])
        }

        create<MavenPublication>("sourcesJar") {
            groupId = "com.github.suwayomi"
            artifactId = "exposed-migrations"
            version = "$libVersion-sources"
            artifact(tasks.named("sourcesJar"))

            from(components["kotlin"])
        }
    }
}
