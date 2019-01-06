dependencies {
    "implementation"(project(":such-annotation"))
    "implementation"(project(":such-fun"))
    "testImplementation"(project(":such-testing"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.1.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.1.0")
}

tasks.test {
    useJUnitPlatform()
}