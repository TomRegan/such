plugins {
    java
    kotlin("jvm") version "1.3.11"
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
        implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.11")
        implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.11")
        implementation("com.google.code.findbugs:jsr305:3.0.1")
        implementation("nl.jqno.equalsverifier:equalsverifier:2.1.1")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.3.11")
        testImplementation("junit:junit:4.12")
        testImplementation("org.mockito:mockito-core:2.2.11")
        testImplementation("org.hamcrest:hamcrest-all:1.3")
        testImplementation("com.google.guava:guava:21.0")
    }
}
