val kotlinVersion by extra("1.3.20")
val junitVersion by extra("5.1.0")

plugins {
    java
    kotlin("jvm") version "1.3.20"
}

allprojects {
    group = "su.ch"
    version = "1.0-SNAPSHOT"

    repositories {
        jcenter()
    }
}

subprojects {

    apply(plugin = "kotlin")

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
        implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
        implementation("com.google.code.findbugs:jsr305:3.0.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
        testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
        testImplementation("org.mockito:mockito-core:2.2.11")
        testImplementation("org.hamcrest:hamcrest-all:1.3")
        testImplementation("com.google.guava:guava:21.0")
    }

    tasks.test {
        useJUnitPlatform()
    }
}
