plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.32"
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
}

java {
    withSourcesJar()
}

dependencies {
    // Align versions of all Kotlin components
    compileOnly(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Logging
    compileOnly("org.slf4j:slf4j-api:1.7.30")
    compileOnly("ch.qos.logback:logback-classic:1.2.3")
    compileOnly("io.github.microutils:kotlin-logging:2.0.6")

    // Exposed ORM
    val exposedVersion = "0.31.1"
    compileOnly("org.jetbrains.exposed:exposed-core:$exposedVersion")
    compileOnly("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    compileOnly("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    compileOnly("org.jetbrains.exposed:exposed-java-time:$exposedVersion")

}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf(
                    "-Xopt-in=kotlin.RequiresOptIn",
            )
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.suwayomi"
            artifactId = "exposed-migrations"
            version = "3.0.0"

            from(components["kotlin"])
        }
    }
}